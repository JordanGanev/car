package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PolicyRequestRepositoryImpl implements PolicyRequestRepository {

    public static final String REQUEST_NOT_FOUND_MESSAGE = "Request with id %d not found!";
    public static final int PENDING_STATUS_ID = 1;

    private final SessionFactory sessionFactory;

    @Autowired
    public PolicyRequestRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PolicyRequestDetails createPolicyRequest(PolicyRequestDetails policyRequestDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(policyRequestDetails.getQuoteRequest());
            session.save(policyRequestDetails);
            session.getTransaction().commit();
            return policyRequestDetails;
        }
    }

    @Override
    public List<PolicyRequestDetails> getAllPolicies(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<PolicyRequestDetails> query = session.createQuery("from PolicyRequestDetails" +
                    " where quoteRequest.user.id = :userId", PolicyRequestDetails.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public void delete(PolicyRequestDetails policy) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(policy);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PolicyRequestDetails> getAllPending() {
        try (Session session = sessionFactory.openSession()) {
            Query<PolicyRequestDetails> query = session.createQuery
                    ("from PolicyRequestDetails where requestStatus.id = :id", PolicyRequestDetails.class);
            query.setParameter("id", PENDING_STATUS_ID);
            return query.list();
        }
    }

    @Override
    public PolicyRequestDetails getById(int requestId) {
        try (Session session = sessionFactory.openSession()) {
            PolicyRequestDetails policy = session.get(PolicyRequestDetails.class, requestId);
            if (policy == null) {
                throw new EntityNotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, requestId));
            }
            return policy;
        }
    }

    @Override
    public void updateStatus(PolicyRequestDetails policy) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(policy);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PolicyRequestDetails> filter(String user, String requestDateMin, String requestDateMax) {
        try (Session session = sessionFactory.openSession()) {
            Query<PolicyRequestDetails> query = session.createQuery
                    ("FROM PolicyRequestDetails" +
                            " WHERE requestStatus.id = :id AND quoteRequest.user.fullName LIKE :user" +
                            " AND requestDate BETWEEN :stDate AND :edDate ", PolicyRequestDetails.class);
            
            query.setParameter("id", PENDING_STATUS_ID);
            query.setParameter("user", "%" + user + "%");
            query.setParameter("stDate", "0000-01-01");
            query.setParameter("edDate", "9999-12-20");

            if (!requestDateMin.isBlank()) {
                query.setParameter("stDate", requestDateMin);
            }
            if (!requestDateMax.isBlank()) {
                query.setParameter("edDate", requestDateMax);
            }
            return query.list();
        }
    }

}
