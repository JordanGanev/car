package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;

import java.util.List;

public interface PolicyRequestRepository {
    PolicyRequestDetails createPolicyRequest(PolicyRequestDetails policyRequestDetails);

    List<PolicyRequestDetails> getAllPolicies(int userId);

    void delete(PolicyRequestDetails policy);

    List<PolicyRequestDetails> getAllPending();

    PolicyRequestDetails getById(int requestId);

    void updateStatus(PolicyRequestDetails policy);

    List<PolicyRequestDetails> filter(String user, String requestDateMin, String requestDateMax);
}
