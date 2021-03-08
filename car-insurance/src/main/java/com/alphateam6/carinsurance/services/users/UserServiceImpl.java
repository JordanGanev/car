package com.alphateam6.carinsurance.services.users;

import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.VerificationToken;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.TokenRepository;
import com.alphateam6.carinsurance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_ERROR = "User with id %d not found!";
    public static final String USER_NOT_ALLOWED_TO_EDIT_ERROR = "User %s is not allowed to edit/delete this user";

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        Optional<User> user = userRepository.getById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_ERROR, id));
        }
    }

    @Override
    public void create(User user) {
        uniqueByUsername(user.getUsername());
        userRepository.create(user);
    }

    @Override
    public void update(User updatedUser, User updateAuthor) {
        validateRightsToEdit(updatedUser, updateAuthor);
        userRepository.update(updatedUser);
    }

    @Override
    public void delete(User userToDelete, User updateAuthor) {
        validateRightsToEdit(userToDelete, updateAuthor);
        userRepository.delete(userToDelete);
    }

    @Override
    public VerificationToken createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken(token);
        return tokenRepository.create(myToken);
    }

    @Override
    public void deleteVerificationToken(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.getByToken(token);
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public void uniqueByUsername(String username) {
        userRepository.uniqueByUsername(username);
    }

    public void validateRightsToEdit(User updatedUser, User updateAuthor) {
        if (!updateAuthor.getUsername().equals(updatedUser.getUsername()) &&
                updateAuthor.getUserAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new InvalidOperationException(String.format(USER_NOT_ALLOWED_TO_EDIT_ERROR, updateAuthor.getUsername()));
        }
    }
}
