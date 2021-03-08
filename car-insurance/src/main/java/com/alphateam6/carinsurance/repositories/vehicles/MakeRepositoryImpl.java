package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.vehicles.Make;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MakeRepositoryImpl implements MakeRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public MakeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Make create(Make make) {
        try (Session session = sessionFactory.openSession()) {
            session.save(make);
            return make;
        }
    }

    public List<Make> getMakes(){
        try (Session session = sessionFactory.openSession()) {
            Query<Make> query = session.createQuery("from Make", Make.class);
            return query.list();
        }
    }

    @Override
    public Make getByMake(String make) {
        try (Session session = sessionFactory.openSession()) {
            List<Make> makes = getMakes(make, session);

            if (makes.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Make with title %s not found!", make));
            }
            return makes.get(0);
        }
    }

    @Override
    public void validateUniqueName(String make) {
        try (Session session = sessionFactory.openSession()) {

            Query<Make> query = session.createQuery("from Make where make = :make", Make.class);
            query.setParameter("make", make);

            List<Make> makes = query.list();

            if (!makes.isEmpty()) {
                throw new DuplicateEntityException(
                        String.format("Make with name %s already exist!", make));
            }
        }
    }

    public boolean existsByMake(String make) {

        try (Session session = sessionFactory.openSession()) {

            Query<Make> query = session.createQuery("from Make where make = :make", Make.class);
            query.setParameter("make", make);

            List<Make> makes = query.list();

            return makes.size() != 0;
        }
    }


    private List<Make> getMakes(String make, Session session) {
        Query<Make> query = session.createQuery("from Make where make = :make", Make.class);
        query.setParameter("make", make);
        return query.list();
    }
}
