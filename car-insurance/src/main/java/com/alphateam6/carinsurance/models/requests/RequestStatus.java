package com.alphateam6.carinsurance.models.requests;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "request_statuses")
@Data
public class RequestStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type")
    private String type;

}
