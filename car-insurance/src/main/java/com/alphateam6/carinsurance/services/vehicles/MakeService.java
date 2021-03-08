package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Make;

import java.util.List;

public interface MakeService {

    List<Make> getMakes();

    Make create(Make make);

    Make getByMake(String make);

    boolean existsByMake(String make);
}
