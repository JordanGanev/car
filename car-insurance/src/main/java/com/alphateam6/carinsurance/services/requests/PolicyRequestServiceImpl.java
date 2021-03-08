package com.alphateam6.carinsurance.services.requests;

import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.requests.PolicyRequestRepository;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alphateam6.carinsurance.services.requests.QuoteRequestServiceImpl.NOT_ALLOWED_TO_EDIT_ERROR;
import static com.alphateam6.carinsurance.services.requests.QuoteRequestServiceImpl.NOT_ALLOWED_TO_VIEW_ERROR;
import static com.alphateam6.carinsurance.validation.utils.Constants.policyDateComparator;

@Service
public class PolicyRequestServiceImpl implements PolicyRequestService {

    private final Mapper mapper;
    private final PolicyRequestRepository requestRepository;
    private final QuoteRequestService quoteRequestService;

    @Autowired
    public PolicyRequestServiceImpl(Mapper mapper, PolicyRequestRepository requestRepository, QuoteRequestService quoteRequestService) {
        this.mapper = mapper;
        this.requestRepository = requestRepository;
        this.quoteRequestService = quoteRequestService;
    }

    @Override
    public PolicyRequestDetails createPolicyRequest(QuoteRequest quoteRequest, PolicyRequestDto policyRequestDto, User user) {
        PolicyRequestDetails policyRequestDetails = mapper.policyRequestMapper(quoteRequest, policyRequestDto, user);
        return requestRepository.createPolicyRequest(policyRequestDetails);
    }

    @Override
    public List<PolicyRequestDetails> getAllPending(User author) {
        if (!author.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_VIEW_ERROR);
        }
        List<PolicyRequestDetails> pendingRequests = requestRepository.getAllPending();
        pendingRequests.sort(policyDateComparator::compare);
        return pendingRequests;
    }

    @Override
    public List<PolicyRequestDetails> getAllPolicies(User userToShow, User userLogged) {
        if (userToShow.getId() != userLogged.getId() && !userLogged.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_VIEW_ERROR);
        }
        List<PolicyRequestDetails> userPolicies = requestRepository.getAllPolicies(userToShow.getId());
        userPolicies.forEach(q -> q.getQuoteRequest().setTotalAmount(quoteRequestService.calculateTotalPremium(q.getQuoteRequest())));
        userPolicies.sort(policyDateComparator::compare);
        return userPolicies;
    }

    @Override
    public void deletePolicyRequest(PolicyRequestDetails policy, User author) {
        if (policy.getUser().getId() != author.getId() && !author.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_EDIT_ERROR);
        }
        requestRepository.delete(policy);
    }

    @Override
    public PolicyRequestDetails getPolicyRequestById(int requestId, User author) {
        if (author.getPolicies().stream().noneMatch(p -> p.getId() == requestId) && !author.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_VIEW_ERROR);
        }
        return requestRepository.getById(requestId);
    }

    @Override
    public void updatePolicyStatus(PolicyRequestDetails policy, User author) {
        if (!author.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_EDIT_ERROR);
        }
        requestRepository.updateStatus(policy);
    }

    @Override
    public List<PolicyRequestDetails> filter(String user, String requestDateMin, String requestDateMax) {
        return requestRepository.filter(user, requestDateMin, requestDateMax);
    }
}
