package br.com.fiap.campusride.service;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.dto.ReservaResponse;
import br.com.fiap.campusride.exception.RecursoNaoEncontradoException;
import br.com.fiap.campusride.exception.RegraNegocioException;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.Reserva;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.repository.ReservaRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final CaronaRepository caronaRepository;
    private final Clock clock;

    public ReservaService(ReservaRepository reservaRepository, CaronaRepository caronaRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.caronaRepository = caronaRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservaResponse reservar(Long caronaId, ReservaRequest request) {
        Carona carona = caronaRepository.findByIdForUpdate(caronaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carona não encontrada"));

        if (carona.estaCancelada()) {
            throw new RegraNegocioException("Carona cancelada não aceita reservas");
        }

        if (carona.estaConcluida()) {
            throw new RegraNegocioException("Carona concluída não aceita reservas");
        }

        if (carona.estaEmAndamento() || !carona.aindaNaoPartiu(LocalDateTime.now(clock))) {
            throw new RegraNegocioException("Carona em andamento não aceita reservas");
        }

        if (!carona.possuiVagasDisponiveis()) {
            throw new RegraNegocioException("Carona não está aberta para reservas");
        }

        if (!carona.estaAberta()) {
            throw new RegraNegocioException("Carona não está aberta para reservas");
        }

        Reserva reserva = new Reserva(carona, request.passageiro());
        carona.adicionarReserva(reserva);

        return ReservaResponse.fromModel(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaResponse cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada"));

        if (reserva.estaCancelada()) {
            throw new RegraNegocioException("Reserva já está cancelada");
        }

        if (reserva.getCarona().estaConcluida()) {
            throw new RegraNegocioException("Reserva de carona concluída não pode ser cancelada");
        }

        reserva.cancelar();
        reserva.getCarona().atualizarSituacaoAposCancelamentoDeReserva();
        return ReservaResponse.fromModel(reserva);
    }
}
