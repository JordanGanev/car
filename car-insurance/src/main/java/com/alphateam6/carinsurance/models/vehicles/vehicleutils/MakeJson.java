package com.alphateam6.carinsurance.models.vehicles.vehicleutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MakeJson {
    @JsonProperty("Make_ID")
    private int id;
    @JsonProperty("Make_Name")
    @NonNull private String name;
}