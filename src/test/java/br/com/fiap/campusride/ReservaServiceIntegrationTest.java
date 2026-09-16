package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.service.ReservaService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class ReservaServiceIntegrationTest {

    @Autowired
    private CaronaRepository caronaRepository;

    @Autowired
    private ReservaService reservaService;

    @Test
    void blocksReservationWhenCaronaHasNoAvailableSeats() {
        Carona carona = caronaRepository.save(new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.MOTO,
                1
        ));

        reservaService.reservar(carona.getId(), new ReservaRequest("Bruno Lima"));

        Carona caronaLotada = caronaRepository.findById(carona.getId()).orElseThrow();
        assertThat(caronaLotada.getSituacao()).isEqualTo(SituacaoCarona.LOTADA);

        assertThatThrownBy(() -> reservaService.reservar(carona.getId(), new ReservaRequest("Carla Reis")))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(exception -> ((ResponseStatusException) exception).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void reopensCaronaWhenConfirmedReservationIsCancelled() {
        Carona carona = caronaRepository.save(new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.MOTO,
                1
        ));

        var reserva = reservaService.reservar(carona.getId(), new ReservaRequest("Bruno Lima"));
        reservaService.cancelar(reserva.id());

        Carona caronaAberta = caronaRepository.findById(carona.getId()).orElseThrow();
        assertThat(caronaAberta.getSituacao()).isEqualTo(SituacaoCarona.ABERTA);

        assertThat(reservaService.reservar(carona.getId(), new ReservaRequest("Carla Reis"))).isNotNull();
    }
}
