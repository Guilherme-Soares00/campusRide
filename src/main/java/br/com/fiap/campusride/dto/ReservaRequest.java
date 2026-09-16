package br.com.fiap.campusride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReservaRequest(
        @NotBlank(message = "Passageiro é obrigatório")
        @Size(max = 100, message = "Passageiro deve ter no máximo 100 caracteres")
        String passageiro
) {

    public ReservaRequest {
        passageiro = passageiro == null ? null : passageiro.trim();
    }
}
