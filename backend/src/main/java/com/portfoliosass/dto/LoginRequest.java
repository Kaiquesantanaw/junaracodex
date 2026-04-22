package com.portfoliosass.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username obrigatório") String username,
        @NotBlank(message = "Senha obrigatória") String password) {
}
