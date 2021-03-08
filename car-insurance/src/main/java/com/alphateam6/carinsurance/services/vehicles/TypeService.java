package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Type;

public interface TypeService {
    Type create(Type type);

    Type getByType(String type);

    void validateUniqueName(String type);

    boolean existsByType(String type);
}
