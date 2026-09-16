package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import br.com.fiap.campusride.model.SituacaoReserva;
import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.repository.ReservaRepository;
import br.com.fiap.campusride.service.CaronaService;
import br.com.fiap.campusride.service.ReservaService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CaronaServiceIntegrationTest {

    @Autowired
    private CaronaRepository caronaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CaronaService caronaService;

    @Autowired
    private ReservaService reservaService;

    @Test
    void listsOnlyOpenFutureRidesOrderedByDeparture() {
        Carona caronaMaisTarde = novaCarona("Motorista 2", LocalDateTime.now().plusDays(2));
        Carona caronaMaisCedo = novaCarona("Motorista 1", LocalDateTime.now().plusDays(1));
        Carona caronaPassada = novaCarona("Motorista 3", LocalDateTime.now().minusDays(1));
        Carona caronaCancelada = novaCarona("Motorista 4", LocalDateTime.now().plusHours(2));
        caronaCancelada.cancelar();

        caronaRepository.saveAll(java.util.List.of(
                caronaMaisTarde,
                caronaMaisCedo,
                caronaPassada,
                caronaCancelada
        ));

        assertThat(caronaService.listar())
                .extracting(response -> response.motorista())
                .containsExactly("Motorista 1", "Motorista 2");
    }

    @Test
    void cancelsRideAndItsReservations() {
        Carona carona = caronaRepository.save(novaCarona("Ana Souza", LocalDateTime.now().plusDays(1)));
        var reserva = reservaService.reservar(carona.getId(), new ReservaRequest("Bruno Lima"));

        var response = caronaService.cancelar(carona.getId());

        assertThat(response.situacao()).isEqualTo(SituacaoCarona.CANCELADA);
        assertThat(reservaRepository.findById(reserva.id()).orElseThrow().getSituacao())
                .isEqualTo(SituacaoReserva.CANCELADA);
    }

    private Carona novaCarona(String motorista, LocalDateTime partida) {
        return new Carona(
                motorista,
                "Campus Norte",
                "Campus Sul",
                partida,
                TipoVeiculo.CARRO,
                4
        );
    }
}
