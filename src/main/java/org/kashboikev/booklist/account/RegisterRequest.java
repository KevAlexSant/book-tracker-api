package org.kashboikev.booklist.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username must be between 4 - 30 characters")
        @Size(min = 4, max = 30)
        String username,

        @NotBlank(message = "Password must be between 8 - 50 characters")
        @Size(min = 3, max = 50)
        String password,

        @NotBlank
        @Size(min = 8, max = 50)
        String confirmPassword) {
}
