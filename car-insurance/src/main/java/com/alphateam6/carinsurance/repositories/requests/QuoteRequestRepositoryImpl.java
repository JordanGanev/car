package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuoteRequestRepositoryImpl implements QuoteRequestRepository {

    public static final String REQUEST_NOT_FOUND_MESSAGE = "Request with id %d not found!";
    private final SessionFactory sessionFactory;

    @Autowired
    public QuoteRequestRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public QuoteRequest create(QuoteRequest quoteRequest) {
        try (Session session = sessionFactory.openSession()) {
            session.save(quoteRequest);
            return quoteRequest;
        }
    }

    public List<PremiumReference> getReferences() {
        try (Session session = sessionFactory.openSession()) {
            Query<PremiumReference> query = session.createQuery
                    ("from PremiumReference", PremiumReference.class);
            return query.list();
        }
    }

    @Override
    public List<QuoteRequest> getAll(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<QuoteRequest> query = session.createQuery("from QuoteRequest" +
                    " where user.id = :userId", QuoteRequest.class);
            query.setParameter("userId", userId);

            return query.list();
        }
    }

    @Override
    public QuoteRequest getQuoteById(int request_id) {

        try (Session session = sessionFactory.openSession()) {
            QuoteRequest quote = session.get(QuoteRequest.class, request_id);

            if (quote == null) {
                throw new EntityNotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, request_id));
            }
            return quote;
        }
    }

    @Override
    public List<PremiumReference> updateReferences(List<PremiumReference> updatedReferences) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("truncate table premium_reference").executeUpdate();
            for (PremiumReference reference : updatedReferences) {
                session.save(reference);
            }
            session.getTransaction().commit();
            return updatedReferences;
        }
    }

    @Override
    public double calculateNetPremium(QuoteRequest quoteRequest) {
        try (Session session = sessionFactory.openSession()) {
            Query<PremiumReference> query = session.createQuery(
                    "FROM PremiumReference " +
                            "WHERE :cc BETWEEN minCC " +
                            "AND maxCC and :carAge BETWEEN min_car_age and max_car_age", PremiumReference.class
            );
            query.setParameter("cc", quoteRequest.getRegisteredVehicle().getCubicCapacity());
            query.setParameter("carAge", (int) quoteRequest.getRegisteredVehicle().getVehicleAge());

            return query.list().get(0).getBase_amount();
        }
    }
}
