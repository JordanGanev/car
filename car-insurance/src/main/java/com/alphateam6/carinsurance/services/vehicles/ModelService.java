package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.VehicleModel;

import java.util.List;

public interface ModelService {
    List<VehicleModel> getModelsByMakeId(int id);

    List<VehicleModel> getModelsByMakeIdAndYear(int id, int year);

    List<VehicleModel> loadData(String make, int modelYear);

    VehicleModel getById(int id);

    VehicleModel getByNameAndYear(String model, String modelYear);

    List<Integer> getModelYears();
}
