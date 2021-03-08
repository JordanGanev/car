package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.vehicles.RegisteredVehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehiclesRepositoryImpl implements VehiclesRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public VehiclesRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<RegisteredVehicle> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<RegisteredVehicle> query = session.createQuery("from RegisteredVehicle", RegisteredVehicle.class);
            return query.list();
        }
    }

    @Override
    public RegisteredVehicle getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            RegisteredVehicle vehicle = session.get(RegisteredVehicle.class, id);

            if (vehicle == null) {
                throw new EntityNotFoundException(String.format("Registered vehicle with id %d not found!", id));
            }
            return vehicle;
        }
    }

    @Override
    public void create(RegisteredVehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.save(vehicle);
        }
    }

    @Override
    public void update(RegisteredVehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(vehicle);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(RegisteredVehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(vehicle);
            session.getTransaction().commit();
        }
    }
}
