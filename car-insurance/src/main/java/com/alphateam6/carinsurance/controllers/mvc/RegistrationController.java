package com.alphateam6.carinsurance.controllers.mvc;

import com.alphateam6.carinsurance.events.OnRegistrationCompleteEvent;
import com.alphateam6.carinsurance.exceptions.DuplicateEntityException;
import com.alphateam6.carinsurance.exceptions.EntityNotFoundException;
import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.VerificationToken;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.models.users.dtos.UserRegistrationDto;
import com.alphateam6.carinsurance.services.users.UserService;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    public static final String ACCOUNT_ACTIVATED_MESSAGE = "You have successfully activated your account.";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid Token";
    public static final String USER_EXISTS_MESSAGE = "User with the same username already exists!";
    public static final String REGISTER_ERROR_MESSAGE = "Unable to register user!";

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public RegistrationController(ApplicationEventPublisher eventPublisher,
                                  UserService userService, Mapper mapper) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                               BindingResult bindingResult, HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "register";
        }
        try {
            User userToCreate = mapper.fromDto(userDto);
            userService.create(userToCreate);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userToCreate, request.getLocale(), appUrl));
            return "registrationConfirm";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", USER_EXISTS_MESSAGE);
            return "register";
        } catch (MailException e) {
            model.addAttribute("error", REGISTER_ERROR_MESSAGE);
            return "register";
        }
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token) {
        try {
            VerificationToken verificationToken = userService.getVerificationToken(token);
            User user = verificationToken.getUser();
            /*Calendar cal = Calendar.getInstance();
            if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                String messageValue = messages.getMessage("auth.message.expired", null, locale);
                model.addAttribute("message", messageValue);
                return "redirect:/badUser.html?lang=" + locale.getLanguage();
            }*/
            user.getUserAuthentication().setEnabled(true);
            userService.update(user, user);
            model.addAttribute("activated", ACCOUNT_ACTIVATED_MESSAGE);
            // delete token after activation
            userService.deleteVerificationToken(verificationToken);
            return "login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", INVALID_TOKEN_MESSAGE);
            return "account-confirm-error";
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/account-confirm-error")
    public String showAccountConfirmErrorPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "account-confirm-error";
    }
}

