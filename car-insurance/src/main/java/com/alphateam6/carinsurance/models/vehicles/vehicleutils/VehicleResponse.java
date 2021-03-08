package com.alphateam6.carinsurance.models.vehicles.vehicleutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleResponse {

    @JsonProperty("Results")
    List<VehicleInputInformation> results;
}
