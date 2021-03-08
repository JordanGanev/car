package com.alphateam6.carinsurance.services;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.users.Authority;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.users.UserAuthentication;
import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;

import java.util.ArrayList;
import java.util.HashSet;

public class ModelFactory {

    public static User createTestUser() {
        User testUser1 = new User();
        UserAuthentication auth = new UserAuthentication("yordan", "ganev");
        auth.setAuthorities(new HashSet<>());
        auth.getAuthorities().add(new Authority("ROLE_USER"));
        testUser1.setId(1);
        testUser1.setUserAuthentication(auth);
        testUser1.setFirstName("TestYordan");
        testUser1.setLastName("TestGanev");
        testUser1.setPhone("0000000000");
        testUser1.setAddress("test address");
        testUser1.setPolicies(new ArrayList<>());
        return testUser1;
    }

    public static User createTestUser2() {
        User testUser2 = new User();
        UserAuthentication auth = new UserAuthentication("pavel", "pavlov");
        auth.setAuthorities(new HashSet<>());
        auth.getAuthorities().add(new Authority("ROLE_USER"));
        testUser2.setId(3);
        testUser2.setUserAuthentication(auth);
        testUser2.setFirstName("TestPavel");
        testUser2.setLastName("TestPavlov");
        testUser2.setPhone("1111111111");
        testUser2.setAddress("test address");
        return testUser2;
    }

    public static User createTestAdmin() {
        User testAdmin = new User();
        UserAuthentication auth = new UserAuthentication("Admin", "Admin");
        auth.setAuthorities(new HashSet<>());
        auth.getAuthorities().add(new Authority("ROLE_ADMIN"));
        testAdmin.setId(2);
        testAdmin.setUserAuthentication(auth);
        testAdmin.setFirstName("TestAdmin");
        testAdmin.setLastName("TestAdmin");
        testAdmin.setPhone("0000000000");
        testAdmin.setAddress("test address");
        return testAdmin;
    }

    public static PolicyRequestDetails createTestPolicy() {
        PolicyRequestDetails testPolicy = new PolicyRequestDetails();
        User user = createTestUser();
        RegisteredVehicle registeredVehicle = new RegisteredVehicle();
        registeredVehicle.setCubicCapacity(2400);
        registeredVehicle.setId(2);
        registeredVehicle.setVehicleModel(new VehicleModel());
        QuoteRequest quoteRequest = new QuoteRequest();
        quoteRequest.setUser(user);
        quoteRequest.setRegisteredVehicle(registeredVehicle);
        quoteRequest.setId(2);
        quoteRequest.setTotalAmount(955.55);
        quoteRequest.setDriverAge(21);
        quoteRequest.setPreviousAccidents(true);
//        quoteRequest.setPolicyRequestDetails(testPolicy);
        testPolicy.setQuoteRequest(quoteRequest);
        testPolicy.setRequestDate("2020-10-16");
        return testPolicy;
    }

    public static QuoteRequest createQuoteRequest(int modelYear) {
        QuoteRequest quoteRequest = new QuoteRequest();
        quoteRequest.setRegisteredVehicle(new RegisteredVehicle());
        quoteRequest.setDriverAge(19);
        quoteRequest.setPreviousAccidents(true);
        quoteRequest.getRegisteredVehicle().setVehicleModel(new VehicleModel());
        quoteRequest.getRegisteredVehicle().getVehicleModel().setModelYear(modelYear);
        quoteRequest.getRegisteredVehicle().setCubicCapacity(2000);
        quoteRequest.getRegisteredVehicle().setRegistrationDate("2016-10-20");
        return quoteRequest;
    }

    public static PremiumReference createReference() {
        PremiumReference reference = new PremiumReference();
        reference.setMinCC(0);
        reference.setMaxCC(2000);
        reference.setMin_car_age(0);
        reference.setMax_car_age(30);
        reference.setBase_amount(200);
        return reference;
    }
}
