package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.models.requests.RequestStatus;

public interface RequestStatusRepository {
    RequestStatus getById(int id);

    RequestStatus getByStatus(String newStatus);
}
