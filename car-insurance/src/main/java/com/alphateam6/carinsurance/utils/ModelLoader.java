package com.alphateam6.carinsurance.utils;

import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.models.vehicles.vehicleutils.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Component
public class ModelLoader {

    private final WebClient webClient;
    private final Mapper mapper;

    @Autowired
    public ModelLoader(WebClient webClient, Mapper mapper) {
        this.webClient = webClient;
        this.mapper = mapper;
    }


    public List<VehicleModel> getAllVehiclesByMakeAndYear(String name, int year) {
        String apiUrl = String.format("https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeyear" +
                        "/make/%s/modelyear/%d/vehicleType/car?format=json",
                name, year);

        VehicleResponse vehicleResponse = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(VehicleResponse.class)
                .block();

        return vehicleResponse != null ? mapper.vehicleMapper(vehicleResponse.getResults(), year) : Collections.emptyList();
    }

}
