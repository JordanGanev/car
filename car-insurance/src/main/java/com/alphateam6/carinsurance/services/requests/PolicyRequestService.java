package com.alphateam6.carinsurance.services.requests;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDto;
import com.alphateam6.carinsurance.models.users.User;

import java.util.List;

public interface PolicyRequestService {

    PolicyRequestDetails createPolicyRequest(QuoteRequest quoteRequest, PolicyRequestDto policyRequestDto, User user);

    List<PolicyRequestDetails> getAllPending(User author);

    List<PolicyRequestDetails> getAllPolicies(User userToShow, User userLogged);

    void deletePolicyRequest(PolicyRequestDetails policy, User author);

    PolicyRequestDetails getPolicyRequestById(int requestId, User author);

    void updatePolicyStatus(PolicyRequestDetails policy, User author);

    List<PolicyRequestDetails> filter(String user, String requestDateMin, String requestDateMax);
}
