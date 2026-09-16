package br.com.fiap.campusride.dto;

import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.validation.CapacidadeVeiculoCompativel;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@CapacidadeVeiculoCompativel
public record CaronaRequest(
        @NotBlank(message = "Motorista é obrigatório")
        @Size(max = 100, message = "Motorista deve ter no máximo 100 caracteres")
        String motorista,

        @NotBlank(message = "Origem é obrigatória")
        @Size(max = 150, message = "Origem deve ter no máximo 150 caracteres")
        String origem,

        @NotBlank(message = "Destino é obrigatório")
        @Size(max = 150, message = "Destino deve ter no máximo 150 caracteres")
        String destino,

        @NotNull(message = "Data e hora de partida são obrigatórias")
        @Future(message = "deve ser uma data futura")
        LocalDateTime dataHoraPartida,

        @NotNull(message = "Tipo de veículo é obrigatório")
        TipoVeiculo tipoVeiculo,

        @NotNull(message = "Quantidade de vagas é obrigatória")
        @Min(value = 1, message = "Quantidade de vagas deve ser no mínimo 1")
        Integer vagasTotais
) {

    public CaronaRequest {
        motorista = normalizar(motorista);
        origem = normalizar(origem);
        destino = normalizar(destino);
    }

    private static String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
