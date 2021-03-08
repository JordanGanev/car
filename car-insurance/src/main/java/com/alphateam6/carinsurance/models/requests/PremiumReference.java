package com.alphateam6.carinsurance.models.requests;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "premium_reference")
public class PremiumReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "min_cc")
    private int minCC;

    @Column(name = "max_cc")
    private int maxCC;

    @Column(name = "car_age_min")
    private int min_car_age;

    @Column(name = "car_age_max")
    private int max_car_age;

    @Column(name = "base_amount")
    private double base_amount;
}
