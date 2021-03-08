package com.alphateam6.carinsurance.models.vehicles.vehicleutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MakeResponse {

    @JsonProperty("Results")
    List<MakeJson> results;

}
