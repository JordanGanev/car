package com.alphateam6.carinsurance.models.requests.dtos;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class RequestCreateDto {

    @Min(value = 0, message = "Please select a make!")
    private int make_id;
    @Min(value = 0, message = "Please select Model Year!")
    private int model_year;
    @Min(value = 0, message = "Please select a model!")
    private int model_id;
    @Min(value = 1, message = "Please select Cubic Capacity!")
    private int cubicCapacity;

    private String firstRegistrationDate;

    @Min(value = 16, message = "Driver's age should be at least 16")
    private int driverAge;
    private boolean previousAccidents;

}
