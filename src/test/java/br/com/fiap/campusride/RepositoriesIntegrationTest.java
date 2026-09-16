package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.Reserva;
import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.repository.ReservaRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RepositoriesIntegrationTest {

    @Autowired
    private CaronaRepository caronaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Test
    void persistsCaronaAndReserva() {
        Carona carona = caronaRepository.save(new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.CARRO,
                4
        ));

        Reserva reserva = reservaRepository.save(new Reserva(carona, "Bruno Lima"));

        assertThat(caronaRepository.count()).isEqualTo(1);
        assertThat(reservaRepository.findById(reserva.getId())).isPresent();
    }
}
