package com.alphateam6.carinsurance.models.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.models.vehicles.Type;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_models")
@Data
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "make_id")
    private Make make;

    @Column(name = "model")
    private String model;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

    @Column(name = "model_year")
    private int modelYear;

}
