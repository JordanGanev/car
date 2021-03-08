package com.alphateam6.carinsurance.models.requests.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyRequestDisplayDto {

    private int requestId;
    private int userId;
    private String userFullName;
    private String vehicleMake;
    private String vehicleModel;
    private String requestDate;

    private int cubicCapacity;
    private int driverAge;
    private boolean prevAccidents;
    private double totalAmount;
    private String effectiveDate;
    private String imageUrl;
    private double policyAmount;

    private String status = "Not requested";
    private boolean isRequested = false;

    public PolicyRequestDisplayDto() {
    }

    public PolicyRequestDisplayDto(int request_id, int userId, String userFullName,
                                   String make, String vehicleModel, String requestDate) {
        this.requestId = request_id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.vehicleMake = make;
        this.vehicleModel = vehicleModel;
        this.requestDate = requestDate;
    }

    public PolicyRequestDisplayDto(int requestId,
                                   int userId,
                                   String userFullName,
                                   String vehicleMake,
                                   String vehicleModel,
                                   String requestDate,
                                   int cubicCapacity,
                                   int driverAge,
                                   boolean prevAccidents,
                                   double totalAmount,
                                   String effectiveDate,
                                   String imageUrl,
                                   String status,
                                   double policyAmount) {

        this(requestId, userId, userFullName, vehicleMake, vehicleModel, requestDate);
        this.cubicCapacity = cubicCapacity;
        this.driverAge = driverAge;
        this.prevAccidents = prevAccidents;
        this.totalAmount = totalAmount;
        this.effectiveDate = effectiveDate;
        this.imageUrl = imageUrl;
        this.status = status;
        this.isRequested = true;
        this.policyAmount = policyAmount;
    }
}
