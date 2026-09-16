package br.com.fiap.campusride.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservaRequest(@NotBlank String passageiro) {
}
