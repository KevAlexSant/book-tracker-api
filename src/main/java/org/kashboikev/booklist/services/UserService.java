package org.kashboikev.booklist.services;

import org.kashboikev.booklist.models.Role;
import org.kashboikev.booklist.models.User;
import org.kashboikev.booklist.models.dto.RegisterRequest;
import org.kashboikev.booklist.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new IllegalStateException("Username already exists");
        }

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.USER);
        userRepository.save(user);



    }


}
