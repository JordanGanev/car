package com.alphateam6.carinsurance.configuration;

import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private final SessionFactory sessionFactory;

    @Autowired
    public ScheduledTasks(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Scheduled(fixedRate = 86400000)
    public void cleanDatabase(){
        try (Session session = sessionFactory.openSession()) {
            Query<QuoteRequest> query = session.createQuery("from QuoteRequest where user = null ", QuoteRequest.class);
            List<QuoteRequest> queriesToDelete = query.list();
            session.beginTransaction();
            for (QuoteRequest quoteRequest: queriesToDelete) {
                session.delete(quoteRequest);
            }
            session.getTransaction().commit();
        }
    }

}
