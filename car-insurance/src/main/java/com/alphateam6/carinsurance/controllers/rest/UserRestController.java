package com.alphateam6.carinsurance.controllers.rest;

import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.users.dtos.UserRegistrationDto;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.utils.Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Api(tags = "User Controller: responsible for user CRUD operations", value = "Users")
public class UserRestController {

    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public UserRestController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @ApiOperation("Returns a list of all users currently in the database")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @ApiOperation("Returns information about a particular user")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation("Register a new user")
    @PostMapping
    public User create(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        try {
            User userToCreate = mapper.fromDto(userRegistrationDto);
            userService.create(userToCreate);
            return userToCreate;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
