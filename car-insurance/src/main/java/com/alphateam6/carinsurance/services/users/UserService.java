package com.alphateam6.carinsurance.services.users;


import com.alphateam6.carinsurance.models.VerificationToken;
import com.alphateam6.carinsurance.models.users.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User updatedUser, User author);

    void delete(User user, User username);

    VerificationToken createVerificationToken(User user, String token);

    void deleteVerificationToken(VerificationToken verificationToken);

    VerificationToken getVerificationToken(String token);
}
