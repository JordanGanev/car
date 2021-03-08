package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Make;

import java.util.List;

public interface MakeRepository {

    List<Make> getMakes();

    Make create(Make make);

    Make getByMake(String make);

    void validateUniqueName(String make);

    boolean existsByMake(String make);

}
