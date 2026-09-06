package org.kashboikev.booklist.controllers;

import org.kashboikev.booklist.models.dto.RegisterRequest;
import org.kashboikev.booklist.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(RegisterRequest registerRequest) {
        userService.register(registerRequest);

        return "redirect:/login";
    }


}
