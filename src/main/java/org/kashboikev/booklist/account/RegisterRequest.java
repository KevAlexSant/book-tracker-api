package org.kashboikev.booklist.account;

public record RegisterRequest(String username, String password, String confirmPassword) {
}
