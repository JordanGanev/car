package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.models.vehicles.VehicleModel;
import com.alphateam6.carinsurance.repositories.vehicles.MakeRepository;
import com.alphateam6.carinsurance.repositories.vehicles.ModelRepository;
import com.alphateam6.carinsurance.repositories.vehicles.TypeRepository;
import com.alphateam6.carinsurance.utils.ModelLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleModelServiceTests {

    @InjectMocks
    MakeServiceImpl makeService;

    @InjectMocks
    ModelServiceImpl modelService;

    @InjectMocks
    TypeServiceImpl typeService;

    @Mock
    MakeService mockMakeService;

    @Mock
    TypeService mockTypeService;

    @Mock
    TypeRepository mockTypeRepository;

    @Mock
    MakeRepository mockMakeRepository;

    @Mock
    WebClient mockWebClient;

    @Mock
    ModelRepository mockModelRepository;

    @Mock
    ModelLoader mockModelLoader;

/*    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }*/

    @Test
    public void loadData_ShouldThrow_WhenModelsAlreadyExist() {
        //Arrange
        List<VehicleModel> models = new ArrayList<>();
        models.add(new VehicleModel());
        //Mockito.when(mockMakeRepository.getByMake("Volvo")).thenReturn(new Make());
        when(mockModelRepository.getByMakeAndYear(Mockito.anyString(),Mockito.anyInt())).thenReturn(models);
        //Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> modelService.loadData(Mockito.anyString(), Mockito.anyInt()));

    }

    @Test
    public void loadData_ShouldObtainVehicleInfo_WhenItDoesNotAlreadyExist() {
        //Arrange
        List<VehicleModel> models = new ArrayList<>();
        models.add(new VehicleModel());
        when(mockModelRepository.getByMakeAndYear(Mockito.anyString(), Mockito.anyInt())).thenReturn(new ArrayList<>());
        when(mockModelLoader.getAllVehiclesByMakeAndYear(Mockito.anyString(), Mockito.anyInt())).thenReturn(models);

        //Act
        modelService.loadData(Mockito.anyString(), Mockito.anyInt());

        //Assert
        Mockito.verify(mockModelRepository, Mockito.times(1)).loadData(models);
    }

    @Test
    public void getMakes_ShouldReturnAllVehicleMakes() {
        //Arrange
        Mockito.when(mockMakeRepository.getMakes()).thenReturn(new ArrayList<>());
        //Act
        makeService.getMakes();
        // Assert
        Mockito.verify(mockMakeRepository, Mockito.times(1)).getMakes();
    }

    @Test
    public void create_ShouldCreate_WhenCorrectInfoPassed() {
        //Arrange
        Mockito.when(mockMakeRepository.create(new Make())).thenReturn(new Make());
        //Act
        makeService.create(new Make());
        // Assert
        Mockito.verify(mockMakeRepository, Mockito.times(1)).create(new Make());
    }

    @Test
    public void getByMake_ShouldReturnCorrect_WhenCorrectMakeRequested() {
        //Arrange
        Mockito.when(mockMakeRepository.getByMake(Mockito.anyString())).thenReturn(new Make());
        //Act
        makeService.getByMake("Volvo");
        // Assert
        Mockito.verify(mockMakeRepository, Mockito.times(1)).getByMake("Volvo");
    }

    @Test
    public void existsByMake_ShouldReturnTrue_WhenMakeExists() {
        //Arrange
        Mockito.when(mockMakeRepository.existsByMake(Mockito.anyString())).thenReturn(true);
        // Act, Assert
        Assertions.assertTrue(makeService.existsByMake("Volvo"));
    }
}
