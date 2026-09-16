package br.com.fiap.campusride.carona;

import java.util.List;
import org.springframework.stereotype.Service;

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
}
