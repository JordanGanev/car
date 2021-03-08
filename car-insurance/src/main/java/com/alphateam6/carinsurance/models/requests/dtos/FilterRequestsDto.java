package com.alphateam6.carinsurance.models.requests.dtos;

import lombok.Data;

@Data
public class FilterRequestsDto {

    public String user;
    public String requestDateMin;
    public String requestDateMax;
}
