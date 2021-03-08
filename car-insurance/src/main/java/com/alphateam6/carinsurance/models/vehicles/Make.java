package com.alphateam6.carinsurance.models.vehicles;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vehicle_makes")
@RequiredArgsConstructor
@NoArgsConstructor
public class Make {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "make")
    @NonNull private String make;

}
