package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.repositories.vehicles.ModelRepository;
import com.alphateam6.carinsurance.utils.ModelLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelLoader modelLoader;
    private final WebClient webClient;
    private final MakeService makeService;
    private final TypeService typeService;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository,
                            @Lazy ModelLoader modelLoader, WebClient webClient,
                            MakeService makeService,
                            TypeService typeService) {
        this.modelRepository = modelRepository;
        this.modelLoader = modelLoader;
        this.makeService = makeService;
        this.webClient = webClient;
        this.typeService = typeService;
    }

    @Override
    public List<VehicleModel> getModelsByMakeId(int id) {
        return modelRepository.getModelsByMakeId(id);
    }

    @Override
    public List<VehicleModel> getModelsByMakeIdAndYear(int id, int year) {
        return modelRepository.getModelsByMakeIdAndYear(id, year);
    }

    @Override
    public List<VehicleModel> loadData(String make, int modelYear) {
        if(modelRepository.getByMakeAndYear(make, modelYear).size() != 0) {
            throw new DuplicateEntityException("Models with such parameters already exist in the database!");
        }

        List<VehicleModel> modelsToUpdate = modelLoader.getAllVehiclesByMakeAndYear(make, modelYear);
        return modelRepository.loadData(modelsToUpdate);
    }

    @Override
    public VehicleModel getById(int id) {
        return modelRepository.getById(id);
    }

    @Override
    public VehicleModel getByNameAndYear(String model, String modelYear) {
        return modelRepository.getByModelAndYear(model, modelYear);
    }

    @Override
    public List<Integer> getModelYears() {
        return modelRepository.getModelYears();
    }

}
