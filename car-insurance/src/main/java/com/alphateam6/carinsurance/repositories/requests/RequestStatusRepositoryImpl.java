package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.requests.RequestStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestStatusRepositoryImpl implements RequestStatusRepository {

    public static final String STATUS_NOT_FOUND_BY_ID_MESSAGE = "Status with id %d not found!";
    public static final String STATUS_NOT_FOUND_BY_NAME_MESSAGE = "Status %s not found!";

    private final SessionFactory sessionFactory;

    @Autowired
    public RequestStatusRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public RequestStatus getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            RequestStatus requestStatus = session.get(RequestStatus.class, id);
            if (requestStatus == null) {
                throw new EntityNotFoundException(String.format(STATUS_NOT_FOUND_BY_ID_MESSAGE, id));
            }
            return requestStatus;
        }
    }

    @Override
    public RequestStatus getByStatus(String newStatus) {
        try (Session session = sessionFactory.openSession()) {
            Query<RequestStatus> query = session.createQuery
                    ("from RequestStatus where type = :newStatus", RequestStatus.class);
            query.setParameter("newStatus", newStatus);

            List<RequestStatus> statuses = query.list();

            if (statuses.isEmpty()) {
                throw new EntityNotFoundException(String.format(STATUS_NOT_FOUND_BY_NAME_MESSAGE, newStatus));
            }
            return statuses.get(0);
        }
    }
}
