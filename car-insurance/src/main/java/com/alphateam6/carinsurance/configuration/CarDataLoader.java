package com.alphateam6.carinsurance.configuration;

import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.models.vehicles.vehicleutils.MakeJson;
import com.alphateam6.carinsurance.services.vehicles.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class CarDataLoader {

    private final ModelService modelService;

    @Autowired
    public CarDataLoader(ModelService modelService) {
        this.modelService = modelService;
    }

    //TODO Add additional functionality to extend vehicles database
/*    @PostConstruct
    public void init() {
        loadData();
    }*/

    public void loadData() {

        //Fetch data
        //List<Make> makes = getAllMakes();
        List<MakeJson> popularMakes = generatePopularMakes();
        List<VehicleModel> vehicleModels = new ArrayList<>();

        for (MakeJson make : popularMakes) {
            for (int year = 2020; year > 2018; year--) {
                modelService.loadData(make.getName(), year);
            }
        }
    }

    private List<MakeJson> generatePopularMakes() {
        List<MakeJson> makes = new ArrayList<>();
        makes.add(new MakeJson("Audi"));
        makes.add(new MakeJson("Alfa Romeo"));
        makes.add(new MakeJson("BMW"));
        makes.add(new MakeJson("Fiat"));
        makes.add(new MakeJson("Honda"));
        makes.add(new MakeJson("Mercedes"));
        makes.add(new MakeJson("Volkswagen"));
        makes.add(new MakeJson("Volvo"));

        return makes;
    }
}
