package com.alphateam6.carinsurance.controllers.mvc;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDto;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.services.requests.PolicyRequestService;
import com.alphateam6.carinsurance.services.requests.QuoteRequestService;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.services.vehicles.MakeService;
import com.alphateam6.carinsurance.services.vehicles.ModelService;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/requests")
@SessionAttributes("quoteRequest")
public class QuoteRequestController {

    private final QuoteRequestService quoteRequestService;
    private final PolicyRequestService policyRequestService;
    private final MakeService makeService;
    private final ModelService modelService;
    private final Mapper mapper;
    private final UserService userService;

    @Autowired
    public QuoteRequestController(QuoteRequestService quoteRequestService,
                                  PolicyRequestService policyRequestService, Mapper mapper,
                                  MakeService makeService,
                                  UserService userService,
                                  ModelService modelService) {
        this.quoteRequestService = quoteRequestService;
        this.policyRequestService = policyRequestService;
        this.makeService = makeService;
        this.mapper = mapper;
        this.userService = userService;
        this.modelService = modelService;
    }

    @ModelAttribute
    public QuoteRequest quoteRequest() {
        return new QuoteRequest();
    }

    @GetMapping("/new")
    public String getCreateQuotePage(Model model, HttpServletRequest request) {
        loadAttributes(model, new RequestCreateDto());

        if (request.getUserPrincipal() != null) {
            String username = request.getUserPrincipal().getName();
            User userLogged = userService.getByUsername(username);
            request.getSession().setAttribute("userLogged", userLogged);
        }
        return "index";
    }

    @PostMapping("/new")
    public String handleCreateQuote(@Valid @ModelAttribute("quoteRequestDto") RequestCreateDto quoteRequestDto,
                                    BindingResult bindingResult,
                                    Model model, Principal principal) {

        User user = null;
        if(principal != null) {
            user = userService.getByUsername(principal.getName());
        }

        loadAttributes(model, quoteRequestDto);

        if (bindingResult.hasErrors()) {
            return "index";
        }

        QuoteRequest request;
        try {
            request = quoteRequestService.createQuoteRequest(quoteRequestDto, user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        model.addAttribute("quoteRequest", request);
        return "index";
    }

    @PostMapping("/new/policy_request")
    public String handleRequestPolicy(@ModelAttribute PolicyRequestDto policyRequestDto,
                                      @ModelAttribute("quoteRequest") QuoteRequest request, Model model,
                                      Principal principal) {
        User user = userService.getByUsername(principal.getName());

        policyRequestService.createPolicyRequest(request, policyRequestDto, user);

        loadAttributes(model, new RequestCreateDto());
        model.addAttribute("quoteRequest", request);

        return "redirect:/users/" + user.getId();
    }

    @PostMapping("/{request_id}/new")
    public String getRequestPolicyFromUserPage(@PathVariable int request_id, Model model, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            QuoteRequest quote = quoteRequestService.getQuoteRequestById(request_id, user);
            quote.setTotalAmount(quoteRequestService.calculateTotalPremium(quote));
            loadAttributes(model, new RequestCreateDto());
            model.addAttribute("quoteRequest", quote);
            return "index";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private void loadAttributes(Model model, RequestCreateDto requestCreateDto) {
        model.addAttribute("quoteRequestDto", requestCreateDto);
        model.addAttribute("allMakes", makeService.getMakes());
        model.addAttribute("policyRequestDTO", new PolicyRequestDto());
        model.addAttribute("modelYears", modelService.getModelYears());
    }
}
