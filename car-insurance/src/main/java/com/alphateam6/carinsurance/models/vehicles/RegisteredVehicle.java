package com.alphateam6.carinsurance.models.vehicles;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vehicle_register")
@Data
public class RegisteredVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "registration_plate")
    private String registrationPlate;

    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private VehicleModel vehicleModel;

    @Column(name = "cubic_capacity")
    private int cubicCapacity;

    @Column(name = "first_registration_date")
    public String registrationDate;

    @Transient
    public long vehicleAge = getVehicleAge();

    public long getVehicleAge() {
        return (registrationDate != null) ?
                ChronoUnit.YEARS.between(LocalDate.parse(registrationDate), LocalDate.now()) : 0;
    }

    public String getMake() {
        return vehicleModel.getMake().getMake();
    }

    public String getVehicleFullName() {
        return getMake() + " " + getVehicleModel().getModel();
    }

}
