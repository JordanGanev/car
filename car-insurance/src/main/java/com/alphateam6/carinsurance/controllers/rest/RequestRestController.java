package com.alphateam6.carinsurance.controllers.rest;

import com.alphateam6.carinsurance.events.OnPolicyUpdateStatus;
import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.RequestStatus;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDisplayDto;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.requests.RequestStatusRepository;
import com.alphateam6.carinsurance.services.requests.PolicyRequestService;
import com.alphateam6.carinsurance.services.requests.QuoteRequestService;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.utils.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/requests")
@Api(value = "Requests",
        tags = "Request Controller: responsible for request CRUD operations and quote-related data retrieval")
public class RequestRestController {

    public static final String NO_PRINCIPAL_ERROR = "You must be admin and logged to view the request.";

    private final QuoteRequestService quoteRequestService;
    private final PolicyRequestService policyRequestService;
    private final RequestStatusRepository requestStatusRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final Mapper mapper;

    @Autowired
    public RequestRestController(QuoteRequestService quoteRequestService,
                                 PolicyRequestService policyRequestService, RequestStatusRepository requestStatusRepository,
                                 UserService userService, ApplicationEventPublisher eventPublisher, Mapper mapper) {
        this.quoteRequestService = quoteRequestService;
        this.policyRequestService = policyRequestService;
        this.requestStatusRepository = requestStatusRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @ApiOperation("Create a new QuoteRequest which would calculate a quote for the given parameters")
    @PostMapping
    public double create(@ApiParam("Required information for a new Quote request to be created.")
                         @Valid
                         @RequestBody RequestCreateDto requestCreateDto,
                         Principal principal) {
        User user = null;
        if (principal != null) {
            user = userService.getByUsername(principal.getName());
        }

        try {
            return quoteRequestService.createQuoteRequest(requestCreateDto, user).getTotalAmount();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation("Delete an existing quote request. Primarily used for database maintenance purposes. Only accessible by its author or an admin")
    @DeleteMapping("/{requestId}")
    public ResponseEntity<String> delete(@PathVariable int requestId, Principal principal) {
        try {
            User author = userService.getByUsername(principal.getName());
            PolicyRequestDetails policyToDelete = policyRequestService.getPolicyRequestById(requestId, author);
            policyRequestService.deletePolicyRequest(policyToDelete, author);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted successfully!");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{requestId}")
    @ApiOperation("Retrieve a Policy request by its id. Only accessible by its author or an admin.")
    public PolicyRequestDisplayDto getById(@PathVariable int requestId, Principal principal) {
        try {
            User author = userService.getByUsername(principal.getName());
            PolicyRequestDetails policy = policyRequestService.getPolicyRequestById(requestId, author);
            return mapper.fromPolicyRequest(policy, quoteRequestService);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NullPointerException noPrincipal) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, NO_PRINCIPAL_ERROR);
        }
    }

    @PostMapping("/change-status/{requestId}")
    @ApiOperation("Update a Policy request's status. Accessible by admin only after reviewing request details.")
    public PolicyRequestDisplayDto updateStatus(@PathVariable int requestId, @RequestBody String statusUpdate, Principal principal) {
        
        try {
            User author = userService.getByUsername(principal.getName());
            PolicyRequestDetails prToUpdate = policyRequestService.getPolicyRequestById(requestId, author);
            statusUpdate = statusUpdate.substring(0, statusUpdate.length() - 1);
            RequestStatus newStatus = requestStatusRepository.getByStatus(statusUpdate);
            prToUpdate.setRequestStatus(newStatus);
            policyRequestService.updatePolicyStatus(prToUpdate, author);

            eventPublisher.publishEvent(new OnPolicyUpdateStatus(prToUpdate));
            return mapper.fromPolicyRequest(prToUpdate, quoteRequestService);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (MailException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

