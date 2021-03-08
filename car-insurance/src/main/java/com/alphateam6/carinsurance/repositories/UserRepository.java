package com.alphateam6.carinsurance.repositories;

import com.alphateam6.carinsurance.models.users.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAll();

    Optional<User> getById(int id);

    void uniqueByUsername(String name);

    void create(User user);

    void update(User user);

    void delete(User user);

    User getByUsername(String username);

}
