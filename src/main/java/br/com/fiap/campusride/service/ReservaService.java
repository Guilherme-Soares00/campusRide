package br.com.fiap.campusride.service;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.dto.ReservaResponse;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.Reserva;
import br.com.fiap.campusride.repository.CaronaRepository;
import br.com.fiap.campusride.repository.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final CaronaRepository caronaRepository;

    public ReservaService(ReservaRepository reservaRepository, CaronaRepository caronaRepository) {
        this.reservaRepository = reservaRepository;
        this.caronaRepository = caronaRepository;
    }

    @Transactional
    public ReservaResponse reservar(Long caronaId, ReservaRequest request) {
        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));

        if (!carona.estaAberta()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carona não está aberta para reservas");
        }

        if (!carona.possuiVagasDisponiveis()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carona sem vagas disponíveis");
        }

        Reserva reserva = new Reserva(carona, request.passageiro());
        carona.adicionarReserva(reserva);

        return ReservaResponse.fromModel(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaResponse cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));

        reserva.cancelar();
        return ReservaResponse.fromModel(reserva);
    }
}
