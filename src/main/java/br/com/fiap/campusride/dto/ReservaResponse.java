package br.com.fiap.campusride.dto;

import br.com.fiap.campusride.model.Reserva;
import br.com.fiap.campusride.model.SituacaoReserva;
import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        String passageiro,
        LocalDateTime realizadaEm,
        SituacaoReserva situacao
) {

    public static ReservaResponse fromModel(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getPassageiro(),
                reserva.getRealizadaEm(),
                reserva.getSituacao()
        );
    }
}
