package com.alphateam6.carinsurance.controllers.rest;

import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.services.vehicles.MakeService;
import com.alphateam6.carinsurance.services.vehicles.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Api(tags = "Vehicles Controller: responsible for maintaining vehicle database", value = "Vehicles")
public class VehicleRestController {

    private final MakeService makeService;
    private final ModelService modelService;

    @Autowired
    public VehicleRestController(MakeService makeService, ModelService modelService) {
        this.makeService = makeService;
        this.modelService = modelService;
    }

    @ApiOperation("Returns a list of all makes currently in the database")
    @GetMapping("/makes")
    public List<Make> getMakes() {
        return makeService.getMakes();
    }

    @ApiOperation("Returns a list of all vehicle models for a particular make")
    @GetMapping("/makes/{id}/models")
    public List<VehicleModel> getModelsByMakeId(@PathVariable int id) {
        return modelService.getModelsByMakeId(id);
    }

    @ApiOperation("Returns a list of all vehicle models for a particular make and year")
    @GetMapping("/makes/{id}/models/{year}")
    public List<VehicleModel> getModelsByMakeIdAndYear(@PathVariable int id, @PathVariable int year) {
        return modelService.getModelsByMakeIdAndYear(id, year);
    }

    @ApiOperation("Loads new vehicle models in the database")
    @PostMapping("/dataloader")
    public List<VehicleModel> loadVehicles(@ApiParam("Vehicle Make")
                                           @RequestParam(value = "make") String make,
                                           @ApiParam("Model year")
                                           @RequestParam(value = "year") int year) {
        return modelService.loadData(make, year);
    }
}
