package com.alphateam6.carinsurance.repositories.vehicles;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.vehicles.Type;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TypeRepositoryImpl implements TypeRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TypeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Type create(Type type) {
        try (Session session = sessionFactory.openSession()) {
            session.save(type);
            return type;
        }
    }

    @Override
    public Type getByType(String type) {
        try (Session session = sessionFactory.openSession()) {
            List<Type> types = getTypes(type, session);

            if (types.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format("Type %s not found!", type));
            }
            return types.get(0);
        }
    }

    @Override
    public void validateUniqueName(String type) {
        try (Session session = sessionFactory.openSession()) {

            Query<Type> query = session.createQuery("from Type where type = :type", Type.class);
            query.setParameter("type", type);

            List<Type> types = query.list();

            if (!types.isEmpty()) {
                throw new DuplicateEntityException(
                        String.format("Type %s already exist!", type));
            }
        }
    }

    @Override
    public boolean existsByType(String type) {

        try (Session session = sessionFactory.openSession()) {

            Query<Type> query = session.createQuery("from Type where type = :type", Type.class);
            query.setParameter("type", type);

            List<Type> types = query.list();

            return types.size() != 0;
        }
    }


    private List<Type> getTypes(String type, Session session) {
        Query<Type> query = session.createQuery("from Type where type = :type", Type.class);
        query.setParameter("type", type);
        return query.list();
    }
}
