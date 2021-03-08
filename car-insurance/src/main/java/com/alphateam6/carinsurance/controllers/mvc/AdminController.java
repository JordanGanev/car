package com.alphateam6.carinsurance.controllers.mvc;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.ReferenceForm;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.dtos.FilterRequestsDto;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDisplayDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.services.requests.PolicyRequestService;
import com.alphateam6.carinsurance.services.requests.QuoteRequestService;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public static final String NO_PRINCIPAL_ERROR_MESSAGE = "Only Admins have access, authorize yourself first.";
    private final QuoteRequestService quoteRequestService;
    private final PolicyRequestService policyRequestService;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public AdminController(QuoteRequestService quoteRequestService, PolicyRequestService policyRequestService,
                           UserService userService, Mapper mapper) {
        this.quoteRequestService = quoteRequestService;
        this.policyRequestService = policyRequestService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        try {
            User author = userService.getByUsername(principal.getName());
            List<PolicyRequestDetails> requests = policyRequestService.getAllPending(author);

            List<PolicyRequestDisplayDto> result = requests.stream()
                    .map(mapper::fromPolicyRequestForAdminPageResult).collect(Collectors.toList());

            model.addAttribute("requests", result);
            model.addAttribute("filterRequestsDto", new FilterRequestsDto());
            return "adminPortal";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NullPointerException noPrincipal) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, NO_PRINCIPAL_ERROR_MESSAGE);
        }
    }

    @PostMapping("/requests-filter")
    public String filterRequests(@ModelAttribute FilterRequestsDto filterRequestsDto, Model model) {

        List<PolicyRequestDetails> requests = policyRequestService.filter(filterRequestsDto.getUser(),
                filterRequestsDto.getRequestDateMin(), filterRequestsDto.getRequestDateMax());

        List<PolicyRequestDisplayDto> result = requests.stream()
                .map(mapper::fromPolicyRequestForAdminPageResult).collect(Collectors.toList());

        model.addAttribute("requests", result);
        model.addAttribute("filterRequestsDto", filterRequestsDto);
        return "adminPortal";
    }

    @GetMapping("/references")
    public String getReferenceTablePage(Model model, Principal principal) {
        ReferenceForm referenceForm = new ReferenceForm();
        List<PremiumReference> references = quoteRequestService.getAllReferences();
        referenceForm.setReferences(references);
        model.addAttribute("references", referenceForm);

        return "referenceTable";
    }

    @PostMapping("/references")
    public String handleEditReferenceTable(@ModelAttribute ReferenceForm referenceForm, Principal principal) {
        quoteRequestService.updateReferences(referenceForm.getReferences());

        return "redirect:/admin";
    }

}
