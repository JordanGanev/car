package com.alphateam6.carinsurance.models.requests;

import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "requests_details")
@Data
public class PolicyRequestDetails implements Serializable {

    @Id
    @Column(name = "request_id")
    private int request_id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "request_id")
    private QuoteRequest quoteRequest;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private RequestStatus requestStatus;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "request_date")
    private String requestDate;

    @Column(name = "vehicle_doc_image")
    private String imageUrl;

    @Column(name = "quote")
    private double quote;

    public RegisteredVehicle getRegisteredVehicle() {
        return quoteRequest.getRegisteredVehicle();
    }

    public User getUser() {
        return quoteRequest.getUser();
    }

}
