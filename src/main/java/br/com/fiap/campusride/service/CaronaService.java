package br.com.fiap.campusride.service;

import br.com.fiap.campusride.dto.CaronaRequest;
import br.com.fiap.campusride.dto.CaronaDetalheResponse;
import br.com.fiap.campusride.dto.CaronaResponse;
import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.repository.CaronaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CaronaService {

    private final CaronaRepository caronaRepository;

    public CaronaService(CaronaRepository caronaRepository) {
        this.caronaRepository = caronaRepository;
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

    public List<CaronaResponse> listar() {
        return caronaRepository.findAll()
                .stream()
                .map(CaronaResponse::fromModel)
                .toList();
    }

    @Transactional(readOnly = true)
    public CaronaDetalheResponse buscarPorId(Long id) {
        Carona carona = caronaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));

        return CaronaDetalheResponse.fromModel(carona);
    }
}
