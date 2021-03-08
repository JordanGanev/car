package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.models.vehicles.VehicleModel;

import java.util.List;

public interface ModelRepository {

    List<VehicleModel> getModelsByMakeId(int id);

    List<VehicleModel> getModelsByMakeIdAndYear(int id, int year);

    List<VehicleModel> loadData(List<VehicleModel> vehicleModels);

    VehicleModel getById(int id);

    VehicleModel getByModelAndYear(String model, String modelYear);

    List<VehicleModel> getByMakeAndYear(String make, int modelYear);

    List<Integer> getModelYears();
}
