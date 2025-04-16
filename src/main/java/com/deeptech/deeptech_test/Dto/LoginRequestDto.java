package com.deeptech.deeptech_test.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotNull(message = "email cannot be null")
    @NotEmpty(message = "email cannot be empty")
    @Email
    String email;

    @NotNull(message = "password cannot be null")
    @NotEmpty(message = "password cannot be empty")
    String password;
}

