package com.portfoliosass.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username obrigatório") @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres") String username,

        @Email(message = "E-mail inválido") @NotBlank(message = "E-mail obrigatório") String email,

        @NotBlank(message = "Senha obrigatória") @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String password) {
}
