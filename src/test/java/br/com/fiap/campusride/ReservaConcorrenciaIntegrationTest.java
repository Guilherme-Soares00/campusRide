package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.exception.RegraNegocioException;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.TipoVeiculo;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.repository.ReservaRepository;
import br.com.fiap.campusride.service.ReservaService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservaConcorrenciaIntegrationTest {

    @Autowired
    private CaronaRepository caronaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaService reservaService;

    @BeforeEach
    @AfterEach
    void limparBanco() {
        reservaRepository.deleteAll();
        caronaRepository.deleteAll();
    }

    @Test
    void preventsOverbookingWithConcurrentRequests() throws Exception {
        Carona carona = caronaRepository.save(new Carona(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.MOTO,
                1
        ));

        CountDownLatch prontas = new CountDownLatch(2);
        CountDownLatch iniciar = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> primeira = executor.submit(() -> reservar(carona.getId(), "Bruno Lima", prontas, iniciar));
            Future<Boolean> segunda = executor.submit(() -> reservar(carona.getId(), "Carla Reis", prontas, iniciar));

            assertThat(prontas.await(5, TimeUnit.SECONDS)).isTrue();
            iniciar.countDown();

            List<Boolean> resultados = List.of(
                    primeira.get(10, TimeUnit.SECONDS),
                    segunda.get(10, TimeUnit.SECONDS)
            );

            assertThat(resultados).containsExactlyInAnyOrder(true, false);
            assertThat(reservaRepository.count()).isEqualTo(1);
        }
    }

    private boolean reservar(Long caronaId, String passageiro, CountDownLatch prontas, CountDownLatch iniciar)
            throws InterruptedException {
        prontas.countDown();
        iniciar.await();

        try {
            reservaService.reservar(caronaId, new ReservaRequest(passageiro));
            return true;
        } catch (RegraNegocioException exception) {
            return false;
        }
    }
}
