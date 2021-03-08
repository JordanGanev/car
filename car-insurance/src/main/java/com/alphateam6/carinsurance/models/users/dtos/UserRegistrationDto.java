package com.alphateam6.carinsurance.models.users.dtos;

import com.alphateam6.carinsurance.validation.PasswordMatches;
import com.alphateam6.carinsurance.validation.ValidEmail;
import com.alphateam6.carinsurance.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class UserRegistrationDto {

    @ValidEmail(message = "Invalid email address.")
    @Size(min = 2, max = 20, message = "Username should be between 2 & 20 symbols.")
    private String username;

    @Size(min = 2, max = 20, message = "First Name should be between 2 & 20 symbols.")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last Name should be between 2 & 20 symbols.")
    private String lastName;

    @NotBlank(message = "Please enter your phone number.")
    private String phone;
    @NotBlank(message = "Please enter your postal address.")
    private String address;

    @ValidPassword
    private String password;
    private String matchingPassword;
}
