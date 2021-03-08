package com.alphateam6.carinsurance.validation.utils;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;

public interface DateComparator {

    int compare(PolicyRequestDetails p1, PolicyRequestDetails p2);
}
