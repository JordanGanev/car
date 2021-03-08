package com.alphateam6.carinsurance.controllers.mvc;

import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model) {

        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);

        if (request.getUserPrincipal() != null) {
            String username = request.getUserPrincipal().getName();
            User userLogged = userService.getByUsername(username);
            request.getSession().setAttribute("userLogged", userLogged);
        }
        return "login";
    }
}
