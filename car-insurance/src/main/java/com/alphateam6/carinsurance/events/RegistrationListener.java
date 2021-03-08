package com.alphateam6.carinsurance.events;

import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    public static final String REGISTRATION_SUCCESS_MESSAGE =
            "You registered successfully. To confirm your registration, please click on the below link.";
    private final UserService userService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    @Autowired
    public RegistrationListener(UserService userService, @Qualifier("messageSource") MessageSource messages,
                                JavaMailSender mailSender) {
        this.userService = userService;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);

        // create token in DB last -> if sending mail fail -> won't create token
        userService.createVerificationToken(user, token);
    }

    private SimpleMailMessage constructEmailMessage(OnRegistrationCompleteEvent event, User user, String token) {
        String recipientAddress = user.getUserAuthentication().getUsername();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSucc", null, REGISTRATION_SUCCESS_MESSAGE,
                event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        return email;
    }
}


