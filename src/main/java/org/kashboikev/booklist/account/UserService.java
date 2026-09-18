package org.kashboikev.booklist.account;

import org.kashboikev.booklist.exceptions.PasswordsDoNotMatchException;
import org.kashboikev.booklist.exceptions.UsernameNotAvailable;
import org.kashboikev.booklist.securityconfig.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new UsernameNotAvailable("Username is already taken");
        }

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}
