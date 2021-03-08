package com.alphateam6.carinsurance.repositories;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.VerificationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenRepositoryImpl implements TokenRepository {


    public static final String TOKEN_NOT_FOUND_ERROR = "Token %s not found!";
    private final SessionFactory sessionFactory;

    @Autowired
    public TokenRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public VerificationToken create(VerificationToken token) {
        try (Session session = sessionFactory.openSession()) {
            session.save(token);
        }
        return token;
    }

    @Override
    public void delete(VerificationToken token) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(token);
            session.getTransaction().commit();
        }
    }

    @Override
    public VerificationToken getByToken(String token) {
        try (Session session = sessionFactory.openSession()) {

            Query<VerificationToken> query = session.createQuery
                    ("from VerificationToken where token = :token", VerificationToken.class);
            query.setParameter("token", token);

            List<VerificationToken> tokens = query.list();

            if (tokens.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format(TOKEN_NOT_FOUND_ERROR, token));
            }
            return tokens.get(0);
        }
    }
}
