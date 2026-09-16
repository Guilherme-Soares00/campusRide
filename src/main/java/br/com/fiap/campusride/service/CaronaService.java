package br.com.fiap.campusride.service;

import br.com.fiap.campusride.dto.CaronaRequest;
import br.com.fiap.campusride.dto.CaronaDetalheResponse;
import br.com.fiap.campusride.dto.CaronaResponse;
import br.com.fiap.campusride.exception.RecursoNaoEncontradoException;
import br.com.fiap.campusride.exception.RegraNegocioException;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import br.com.fiap.campusride.repository.CaronaRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CaronaService {

    private final CaronaRepository caronaRepository;
    private final Clock clock;

    public CaronaService(CaronaRepository caronaRepository, Clock clock) {
        this.caronaRepository = caronaRepository;
        this.clock = clock;
    }

    public CaronaResponse criar(CaronaRequest request) {
        Carona carona = new Carona(
                request.motorista(),
                request.origem(),
                request.destino(),
                request.dataHoraPartida(),
                request.tipoVeiculo(),
                request.vagasTotais()
        );

        return CaronaResponse.fromModel(caronaRepository.save(carona));
    }

    @Transactional(readOnly = true)
    public List<CaronaResponse> listar() {
        return caronaRepository.findBySituacaoAndDataHoraPartidaAfterOrderByDataHoraPartidaAsc(
                        SituacaoCarona.ABERTA,
                        LocalDateTime.now(clock)
                )
                .stream()
                .map(CaronaResponse::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public CaronaDetalheResponse buscarPorId(Long id) {
        Carona carona = caronaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carona não encontrada"));

        return CaronaDetalheResponse.fromModel(carona);
    }

    @Transactional
    public CaronaDetalheResponse cancelar(Long id) {
        Carona carona = caronaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carona não encontrada"));

        if (carona.estaConcluida()) {
            throw new RegraNegocioException("Carona concluída não pode ser cancelada");
        }

        if (carona.estaCancelada()) {
            throw new RegraNegocioException("Carona já está cancelada");
        }

        carona.cancelar();
        return CaronaDetalheResponse.fromModel(carona);
    }
}
