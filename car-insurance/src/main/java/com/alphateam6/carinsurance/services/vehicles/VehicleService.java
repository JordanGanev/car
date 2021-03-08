package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;

import java.util.List;

public interface VehicleService {

    List<RegisteredVehicle> getAll();

    RegisteredVehicle getById(int id);

    void create(RegisteredVehicle vehicle);

    void update(RegisteredVehicle vehicle);

    void delete(RegisteredVehicle vehicle);
}
