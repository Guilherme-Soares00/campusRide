package br.com.fiap.campusride.carona;

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
