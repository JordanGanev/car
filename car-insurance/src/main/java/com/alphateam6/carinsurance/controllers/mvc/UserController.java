package com.alphateam6.carinsurance.controllers.mvc;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDisplayDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.users.dtos.UserDisplayDto;
import com.alphateam6.carinsurance.services.requests.PolicyRequestService;
import com.alphateam6.carinsurance.services.requests.QuoteRequestService;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@SessionAttributes({"quotes", "policies", "userInfo"})
public class UserController {

    private final QuoteRequestService quoteRequestService;
    private final PolicyRequestService policyRequestService;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public UserController(QuoteRequestService quoteRequestService, PolicyRequestService policyRequestService, UserService userService, Mapper mapper) {
        this.quoteRequestService = quoteRequestService;
        this.policyRequestService = policyRequestService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/{userId}")
    public String getUserProfilePage(@PathVariable int userId, Model model, Principal principal) {
        try {
            User userToShow = userService.getById(userId);
            User userLogged = userService.getByUsername(principal.getName());
            List<PolicyRequestDetails> userPolicies = policyRequestService.getAllPolicies(userToShow, userLogged);
            List<Integer> quoteIds = userPolicies
                    .stream().map(PolicyRequestDetails::getRequest_id).collect(Collectors.toList());

            List<QuoteRequest> userQuotes = quoteRequestService.getAllQuoteRequests(userToShow, userLogged)
                    .stream()
                    .filter(quote -> !quoteIds.contains(quote.getId()))
                    .collect(Collectors.toList());

            List<PolicyRequestDisplayDto> userQuotesDto = userQuotes
                    .stream().map(mapper::fromQuoteRequest).collect(Collectors.toList());
            List<PolicyRequestDisplayDto> userPoliciesDto = userPolicies.stream()
                    .map(policyRequestDetails -> mapper.fromPolicyRequest(policyRequestDetails, quoteRequestService))
                    .collect(Collectors.toList());

            UserDisplayDto userInfo = mapper.fromUser(userToShow);

            model.addAttribute("quotes", userQuotesDto);
            model.addAttribute("policies", userPoliciesDto);
            model.addAttribute("userInfo", userInfo);
            return "userProfile";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{userId}")
    public String handleUpdateProfile(@PathVariable int userId,
                                      @ModelAttribute UserDisplayDto userInfo,
                                      Model model, Principal principal) {
        try {
            User userToUpdate = userService.getById(userId);
            User userLogged = userService.getByUsername(principal.getName());
            mapper.updateUserInfo(userToUpdate, userInfo);
            userService.update(userToUpdate, userLogged);

            model.addAttribute("userInfo", userInfo);
            return "userProfile";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
