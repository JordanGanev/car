package com.alphateam6.carinsurance.validation.utils;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;

import java.time.LocalDate;

public final class Constants {

    public static final DateComparator policyDateComparator = (PolicyRequestDetails p1, PolicyRequestDetails p2) -> {
        String sDate1 = p1.getRequestDate();
        String sDate2 = p2.getRequestDate();
        LocalDate date1 = LocalDate.parse(sDate1);
        LocalDate date2 = LocalDate.parse(sDate2);
        return date2.compareTo(date1);
    };
}
