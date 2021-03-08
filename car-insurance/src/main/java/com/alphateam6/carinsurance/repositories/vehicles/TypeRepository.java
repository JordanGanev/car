package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Type;

public interface TypeRepository {
    Type create(Type type);

    Type getByType(String type);

    void validateUniqueName(String type);

    boolean existsByType(String type);
}
