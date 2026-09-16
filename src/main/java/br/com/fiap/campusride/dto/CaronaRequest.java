package br.com.fiap.campusride.dto;

import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.validation.CapacidadeVeiculoCompativel;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@CapacidadeVeiculoCompativel
public record CaronaRequest(
        @NotBlank String motorista,
        @NotBlank String origem,
        @NotBlank String destino,
        @NotNull @Future LocalDateTime dataHoraPartida,
        @NotNull TipoVeiculo tipoVeiculo,
        @NotNull @Min(1) Integer vagasTotais
) {
}
