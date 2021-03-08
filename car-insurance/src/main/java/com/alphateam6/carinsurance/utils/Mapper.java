package com.alphateam6.carinsurance.utils;


import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDisplayDto;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDto;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.Authority;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.users.UserAuthentication;
import com.alphateam6.carinsurance.models.users.dtos.UserDisplayDto;
import com.alphateam6.carinsurance.models.users.dtos.UserRegistrationDto;
import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import com.alphateam6.carinsurance.models.vehicles.Type;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.models.vehicles.vehicleutils.VehicleInputInformation;
import com.alphateam6.carinsurance.repositories.requests.RequestStatusRepository;
import com.alphateam6.carinsurance.services.requests.QuoteRequestService;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.services.vehicles.MakeService;
import com.alphateam6.carinsurance.services.vehicles.ModelService;
import com.alphateam6.carinsurance.services.vehicles.TypeService;
import com.alphateam6.carinsurance.services.vehicles.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Mapper {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final ModelService modelService;
    private final MakeService makeService;
    private final TypeService typeService;
    private final RequestStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Mapper(UserService userService,
                  VehicleService vehicleService,
                  ModelService modelService,
                  MakeService makeService, TypeService typeService, RequestStatusRepository statusRepository, PasswordEncoder passwordEncoder) {

        this.modelService = modelService;
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.makeService = makeService;
        this.typeService = typeService;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public QuoteRequest fromDto(RequestCreateDto requestCreateDto, User user) {

        QuoteRequest quoteRequest = new QuoteRequest();
        RegisteredVehicle vehicle = registerVehicle(requestCreateDto);
        quoteRequest.setRegisteredVehicle(vehicle);
        quoteRequest.setUser(user);
        quoteRequest.setDriverAge(requestCreateDto.getDriverAge());
        quoteRequest.setPreviousAccidents(requestCreateDto.isPreviousAccidents());

        return quoteRequest;
    }

    private RegisteredVehicle registerVehicle(RequestCreateDto requestCreateDto) {

        RegisteredVehicle vehicle = new RegisteredVehicle();
        //String modelYear = requestCreateDto.getFirstRegistrationDate().substring(0, 4);

        vehicle.setVehicleModel(modelService.getById(requestCreateDto.getModel_id()));
        vehicle.setCubicCapacity(requestCreateDto.getCubicCapacity());
        vehicle.setRegistrationDate(requestCreateDto.getFirstRegistrationDate());
        vehicleService.create(vehicle);
        return vehicle;
    }

    public User fromDto(UserRegistrationDto userDto) {

        User user = new User();
        UserAuthentication userAuth = new UserAuthentication();
        userAuth.setUsername(userDto.getUsername());
        userAuth.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserAuthentication(userAuth);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        /* creates Authority -> sets role and user's auth and then sets the new Authority in user's UserAuth... */
        Set<Authority> roles = new HashSet<>();
        Authority userRole = new Authority();
        userRole.setAuthority("ROLE_USER");
        userRole.setUserAuthentication(user.getUserAuthentication());
        roles.add(userRole);
        user.getUserAuthentication().setAuthorities(roles);
        return user;
    }

    public PolicyRequestDetails policyRequestMapper(QuoteRequest quoteRequest, PolicyRequestDto dto, User user) {
        quoteRequest.setUser(user);
        PolicyRequestDetails policyRequestDetails = new PolicyRequestDetails();
        policyRequestDetails.setQuoteRequest(quoteRequest);
        policyRequestDetails.setEffectiveDate(dto.getEffectiveDate());
        policyRequestDetails.setRequestDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        policyRequestDetails.setImageUrl(dto.getDocUrl());
        policyRequestDetails.setRequestStatus(statusRepository.getById(1));
        policyRequestDetails.setQuote(quoteRequest.getTotalAmount());
        return policyRequestDetails;
    }

    public PolicyRequestDisplayDto fromPolicyRequest(PolicyRequestDetails policy, QuoteRequestService quoteRequestService) {
        return new PolicyRequestDisplayDto(
                policy.getRequest_id(),
                policy.getUser().getId(),
                policy.getUser().getFullName(),
                policy.getRegisteredVehicle().getMake(),
                policy.getRegisteredVehicle().getVehicleModel().getModel(),
                policy.getRequestDate(),
                policy.getRegisteredVehicle().getCubicCapacity(),
                policy.getQuoteRequest().getDriverAge(),
                policy.getQuoteRequest().isPreviousAccidents(),
                policy.getQuote(),
                policy.getEffectiveDate(),
                policy.getImageUrl(),
                policy.getRequestStatus().getType(),
                policy.getQuote());
    }

    public PolicyRequestDisplayDto fromPolicyRequestForAdminPageResult(PolicyRequestDetails policy) {
        return new PolicyRequestDisplayDto(
                policy.getRequest_id(),
                policy.getQuoteRequest().getUser().getId(),
                policy.getQuoteRequest().getUser().getFullName(),
                policy.getQuoteRequest().getRegisteredVehicle().getMake(),
                policy.getRegisteredVehicle().getVehicleModel().getModel(),
                policy.getRequestDate());
    }

    public PolicyRequestDisplayDto fromQuoteRequest(QuoteRequest quote) {
        PolicyRequestDisplayDto notRequested = new PolicyRequestDisplayDto();
        notRequested.setRequestId(quote.getId());
        notRequested.setVehicleMake(quote.getRegisteredVehicle().getMake());
        notRequested.setVehicleModel(quote.getRegisteredVehicle().getVehicleModel().getModel());
        notRequested.setCubicCapacity(quote.getRegisteredVehicle().getCubicCapacity());
        notRequested.setDriverAge(quote.getDriverAge());
        notRequested.setPrevAccidents(quote.isPreviousAccidents());
        notRequested.setTotalAmount(quote.getTotalAmount());
        return notRequested;
    }

    public UserDisplayDto fromUser(User userToShow) {
        UserDisplayDto userDisplayDto = new UserDisplayDto();
        userDisplayDto.setFirstName(userToShow.getFirstName());
        userDisplayDto.setLastName(userToShow.getLastName());
        userDisplayDto.setAddress(userToShow.getAddress());
        userDisplayDto.setPhone(userToShow.getPhone());
        return userDisplayDto;
    }

    public void updateUserInfo(User userToUpdate, UserDisplayDto userInfo) {
        userToUpdate.setFirstName(userInfo.getFirstName());
        userToUpdate.setLastName(userInfo.getLastName());
        userToUpdate.setPhone(userInfo.getPhone());
        userToUpdate.setAddress(userInfo.getAddress());
    }

    public List<VehicleModel> vehicleMapper(List<VehicleInputInformation> vehicleInputInformationList, int modelYear) {
        List<VehicleModel> vehicleModels = new ArrayList<>();
        for (VehicleInputInformation vehicleInputInformation : vehicleInputInformationList
        ) {
            VehicleModel vehicleModel = new VehicleModel();

            if (!makeService.existsByMake(vehicleInputInformation.getMake_name())) {
                Make make = new Make(vehicleInputInformation.getMake_name());
                makeService.create(make);
            }

            vehicleModel.setMake(makeService.getByMake(vehicleInputInformation.getMake_name()));
            vehicleModel.setModel(vehicleInputInformation.getModel_name());

            if (!typeService.existsByType(vehicleInputInformation.getVehicleType())) {
                Type type = new Type(vehicleInputInformation.getVehicleType());
                typeService.create(type);
            }
            vehicleModel.setType(typeService.getByType(vehicleInputInformation.getVehicleType()));

            vehicleModel.setModelYear(modelYear);
            vehicleModels.add(vehicleModel);
        }

        return vehicleModels;
    }
}
