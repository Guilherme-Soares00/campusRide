package br.com.fiap.campusride.dto;

import br.com.fiap.campusride.model.TipoVeiculo;
import java.time.LocalDateTime;

public record CaronaRequest(
        String motorista,
        String origem,
        String destino,
        LocalDateTime dataHoraPartida,
        TipoVeiculo tipoVeiculo,
        Integer vagasTotais
) {
}
