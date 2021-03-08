package com.alphateam6.carinsurance.services.users;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.VerificationToken;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.TokenRepository;
import com.alphateam6.carinsurance.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.alphateam6.carinsurance.services.ModelFactory.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    UserServiceImpl userService;

    User testUser;

    @BeforeEach
    public void setup() {
        testUser = createTestUser();
//        MockitoAnnotations.initMocks(this);
    }

    private VerificationToken createToken(User user) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        myToken.setToken("token");
        return myToken;
    }

    @Test
    public void create_ShouldThrow_WhenUserExists() {
        // Arrange
        Mockito.doThrow(DuplicateEntityException.class)
                .when(mockUserRepository)
                .uniqueByUsername(testUser.getUsername());
        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.create(testUser));
    }

    @Test
    public void create_ShouldCreate_WhenUserDoesNotExist() {
        // Arrange
        Mockito.doNothing().when(mockUserRepository).uniqueByUsername(testUser.getUsername());
        // Act
        userService.create(testUser);
        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).create(testUser);
    }

    @Test
    public void update_ShouldThrow_WhenUpdateAuthorHaveNoRights() {
        // Arrange
        User testUser2 = createTestUser2();
        // Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> userService.update(testUser2, testUser));
    }

    @Test
    public void update_ShouldUpdate_WhenUserHaveRights() {
        // Arrange
        User userAdmin = createTestAdmin();
        // Act
        userService.update(testUser, userAdmin);
        userService.update(testUser, testUser);
        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(2)).update(testUser);
    }

    @Test
    public void delete_ShouldThrow_WhenUpdateAuthorHaveNoRights() {
        // Arrange
        User testUser2 = createTestUser2();
        // Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> userService.delete(testUser2, testUser));
    }

    @Test
    public void delete_ShouldUpdate_WhenUserHaveRights() {
        // Arrange
        User userAdmin = createTestAdmin();
        // Act
        userService.delete(testUser, userAdmin);
        userService.delete(testUser, testUser);
        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(2)).delete(testUser);
    }

    @Test
    public void createVerificationToken_ShouldCreateToken() {
        // Arrange
        VerificationToken token = createToken(testUser);
        Mockito.when(tokenRepository.create(token)).thenReturn(token);
        // Act
        userService.createVerificationToken(testUser, token.getToken());
        // Assert
        Mockito.verify(tokenRepository, Mockito.times(1)).create(token);
    }

    @Test
    public void getAll_ShouldReturn_AllUsers() {
        // Arrange
        Mockito.when(mockUserRepository.getAll()).thenReturn(Arrays.asList(testUser, testUser, testUser));
        // Act
        List<User> users = userService.getAll();
        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).getAll();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    public void getById_ShouldThrow_WhenNotFound() {
        // Arrange
        Mockito.when(mockUserRepository.getById(Mockito.anyInt()))
                .thenReturn(Optional.empty());
        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.getById(Mockito.anyInt()));
    }

    @Test
    public void getById_ShouldReturn_UserWhenExist() {
        // Arrange
        Mockito.when(mockUserRepository.getById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));
        // Act
        User user = userService.getById(testUser.getId());
        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).getById(testUser.getId());
        Assertions.assertEquals(testUser.getId(), user.getId());
        Assertions.assertSame(user, testUser);
    }
}
