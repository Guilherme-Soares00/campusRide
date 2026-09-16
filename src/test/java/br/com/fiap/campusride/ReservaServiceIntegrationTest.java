package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.exception.RegraNegocioException;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.service.ReservaService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
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
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Carona não está aberta para reservas");
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

    @Test
    void blocksReservationAfterDepartureTime() {
        Carona carona = caronaRepository.save(new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().minusMinutes(1),
                TipoVeiculo.CARRO,
                2
        ));

        assertThatThrownBy(() -> reservaService.reservar(carona.getId(), new ReservaRequest("Bruno Lima")))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Carona em andamento não aceita reservas");
    }

    @Test
    void blocksReservationWhenCaronaIsCancelled() {
        Carona carona = new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.CARRO,
                2
        );
        carona.cancelar();
        caronaRepository.save(carona);

        assertThatThrownBy(() -> reservaService.reservar(carona.getId(), new ReservaRequest("Bruno Lima")))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Carona cancelada não aceita reservas");
    }
}
