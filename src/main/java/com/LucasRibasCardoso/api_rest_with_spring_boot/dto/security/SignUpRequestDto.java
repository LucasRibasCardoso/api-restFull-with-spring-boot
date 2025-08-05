package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequestDto(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Full name is required") String fullName,
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank(message = "Phone number is required")
        @Pattern(
            regexp = "^\\(\\d{2}\\)\\s\\d{5}-\\d{4}$",
            message = "Phone number must be in the format (XX) XXXXX-XXXX")
        String phone,
    @NotBlank(message = "Password is required") String password,
    @NotBlank(message = "Confirm password is required") String confirmPassword) {}
