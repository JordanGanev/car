package com.alphateam6.carinsurance.models.requests;

import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Data
//@ToString(exclude = {"policyRequestDetails"})
public class QuoteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private RegisteredVehicle registeredVehicle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "driver_age")
    private int driverAge;

    @Column(name = "previous_accidents")
    private boolean previousAccidents;

    /*@Formula("(SELECT pr.base_amount FROM premium_reference pr\n" +
            "WHERE pr.car_age_min < 12 AND pr.car_age_max > 12 " +
            "AND 1000 < pr.max_cc AND 1000 > pr.min_cc)")*/
    @Transient
    private Double totalAmount;

//    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "quoteRequest")
//    private PolicyRequestDetails policyRequestDetails;
}
