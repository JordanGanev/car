package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Type;
import com.alphateam6.carinsurance.repositories.vehicles.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    @Autowired
    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public Type create(Type type) {
        return typeRepository.create(type);
    };

    @Override
    public Type getByType(String type) {
        return typeRepository.getByType(type);
    };

    @Override
    public void validateUniqueName(String type) {
        typeRepository.validateUniqueName(type);
    };

    @Override
    public boolean existsByType(String type) {
        return typeRepository.existsByType(type);
    };
}
