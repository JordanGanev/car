package com.alphateam6.carinsurance.services.vehicles;

import com.alphateam6.carinsurance.models.vehicles.Make;
import com.alphateam6.carinsurance.repositories.vehicles.MakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakeServiceImpl implements MakeService {

    private final MakeRepository makeRepository;

    @Autowired
    public MakeServiceImpl(MakeRepository makeRepository) {
        this.makeRepository = makeRepository;
    }

    @Override
    public List<Make> getMakes() {
        return makeRepository.getMakes();
    }

    @Override
    public Make create(Make make) {
        return makeRepository.create(make);
    }

    @Override
    public Make getByMake(String make) {
        return makeRepository.getByMake(make);
    }

    @Override
    public boolean existsByMake(String make) {
        return makeRepository.existsByMake(make);
    }



}
