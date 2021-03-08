package com.alphateam6.carinsurance.events;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class OnPolicyUpdateStatus extends ApplicationEvent {

    private PolicyRequestDetails policy;

    public OnPolicyUpdateStatus(PolicyRequestDetails policy) {
        super(policy);
        this.policy = policy;
    }
}
