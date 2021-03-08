package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import com.alphateam6.carinsurance.repositories.vehicles.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    VehiclesRepository vehiclesRepository;

    @Autowired
    public VehicleServiceImpl(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }


    @Override
    public List<RegisteredVehicle> getAll() {
        return vehiclesRepository.getAll();
    }

    @Override
    public RegisteredVehicle getById(int id) {
        return vehiclesRepository.getById(id);
    }

    @Override
    public void create(RegisteredVehicle vehicle) {
        vehiclesRepository.create(vehicle);
    }

    @Override
    public void update(RegisteredVehicle vehicle) {
        vehiclesRepository.update(vehicle);
    }

    @Override
    public void delete(RegisteredVehicle vehicle) {
        vehiclesRepository.delete(vehicle);
    }
}
