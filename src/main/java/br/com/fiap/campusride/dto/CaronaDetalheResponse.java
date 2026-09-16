package br.com.fiap.campusride.dto;

import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import br.com.fiap.campusride.model.TipoVeiculo;
import java.time.LocalDateTime;
import java.util.List;

public record CaronaDetalheResponse(
        Long id,
        String motorista,
        String origem,
        String destino,
        LocalDateTime dataHoraPartida,
        TipoVeiculo tipoVeiculo,
        Integer vagasTotais,
        SituacaoCarona situacao,
        List<ReservaResponse> reservas
) {

    public static CaronaDetalheResponse fromModel(Carona carona) {
        List<ReservaResponse> reservas = carona.getReservas()
                .stream()
                .map(ReservaResponse::fromModel)
                .toList();

        return new CaronaDetalheResponse(
                carona.getId(),
                carona.getMotorista(),
                carona.getOrigem(),
                carona.getDestino(),
                carona.getDataHoraPartida(),
                carona.getTipoVeiculo(),
                carona.getVagasTotais(),
                carona.getSituacao(),
                reservas
        );
    }
}
