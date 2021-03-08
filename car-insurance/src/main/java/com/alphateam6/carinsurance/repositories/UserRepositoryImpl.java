package com.alphateam6.carinsurance.repositories;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.users.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    public static final String USER_NOT_FOUND_ERROR = "User with username %s not found!";
    public static final String USER_EXISTS_ERROR = "User with username %s already exist!";

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public Optional<User> getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user.getUserAuthentication());
            session.save(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.update(user.getUserAuthentication());
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = getUsers(username, session);

            if (users.isEmpty()) {
                throw new EntityNotFoundException(
                        String.format(USER_NOT_FOUND_ERROR, username));
            }
            return users.get(0);
        }
    }

    @Override
    public void uniqueByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = getUsers(username, session);

            if (!users.isEmpty()) {
                throw new DuplicateEntityException(
                        String.format(USER_EXISTS_ERROR, username));
            }
        }
    }

    private List<User> getUsers(String username, Session session) {
        Query<User> query = session.createQuery(
                "from User where userAuthentication.username = :username", User.class);
        query.setParameter("username", username);
        return query.list();
    }
}
