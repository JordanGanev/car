package com.alphateam6.carinsurance.models.vehicles.vehicleutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleInputInformation {

    @JsonProperty("Make_ID")
    private int make_id;

    @JsonProperty("Make_Name")
    private String make_name;

    @JsonProperty("Model_ID")
    private int model_id;

    @JsonProperty("Model_Name")
    private String model_name;

    @JsonProperty("VehicleTypeName")
    private String vehicleType;

    private String modelYear;
}
