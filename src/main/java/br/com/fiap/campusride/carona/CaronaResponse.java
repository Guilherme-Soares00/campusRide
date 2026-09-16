package br.com.fiap.campusride.carona;

import java.time.LocalDateTime;

public record CaronaResponse(
        Long id,
        String motorista,
        String origem,
        String destino,
        LocalDateTime dataHoraPartida,
        TipoVeiculo tipoVeiculo,
        Integer vagasTotais,
        SituacaoCarona situacao
) {

    public static CaronaResponse fromModel(Carona carona) {
        return new CaronaResponse(
                carona.getId(),
                carona.getMotorista(),
                carona.getOrigem(),
                carona.getDestino(),
                carona.getDataHoraPartida(),
                carona.getTipoVeiculo(),
                carona.getVagasTotais(),
                carona.getSituacao()
        );
    }
}
