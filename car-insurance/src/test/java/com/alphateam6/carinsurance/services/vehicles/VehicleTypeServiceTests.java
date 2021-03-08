package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.models.vehicles.Type;
import com.alphateam6.carinsurance.repositories.vehicles.TypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleTypeServiceTests {

    @InjectMocks
    TypeServiceImpl typeService;

    @Mock
    TypeRepository typeRepository;

    @Test
    public void create_ShouldCreate_WhenCorrectInfoPassed() {
        //Arrange
        Mockito.when(typeRepository.create(new Type())).thenReturn(new Type());
        //Act
        typeService.create(new Type());
        // Assert
        Mockito.verify(typeRepository, Mockito.times(1)).create(new Type());
    }

    @Test
    public void getByType_ShouldReturn_WhenCorrectTypePassed() {
        //Arrange
        Mockito.when(typeRepository.getByType(Mockito.anyString())).thenReturn(new Type());
        //Act
        typeService.getByType("Car");
        // Assert
        Mockito.verify(typeRepository, Mockito.times(1)).getByType("Car");
    }

    @Test
    public void validateUniqueName_ShouldThrow_WhenIncorrectTypePassed() {
        //Arrange        //Act
        Mockito.doThrow(new DuplicateEntityException("Already exists!"))
                .when(typeRepository).validateUniqueName(Mockito.anyString());
        // Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> typeRepository.validateUniqueName("Car"));
    }
}
