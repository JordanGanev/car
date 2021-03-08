package com.alphateam6.carinsurance.controllers.errorhandlers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@ControllerAdvice(basePackages = {"com.alphateam6.carinsurance.controllers.mvc"})
public class MvcErrorHandler implements ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public String handle(ResponseStatusException ex, Model model) {

        String errorMessage = ex.getReason();
        String codeName = ex.getStatus().name();
        int code = ex.getStatus().value();
        loadErrorPageModels(errorMessage, codeName, code, model);
        return "error";
    }

    /**
     * Error when there is no explicit mapping.
     * To work we must disable two properties in app.prop
     * and this class must implement ErrorController
     */
    @RequestMapping("/error")
    public String handleNoExplicitMapping(Model model) {

        loadErrorPageModels("Something went wrong", "NOT FOUND", 404, model);
        return "error";
    }

    private void loadErrorPageModels(String errorMessage, String codeName, int code, Model model) {
        model.addAttribute("message", errorMessage);
        model.addAttribute("codeName", codeName);
        model.addAttribute("code", code);
    }

    /**
     * Deprecated from spring, must be left like that.
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}
