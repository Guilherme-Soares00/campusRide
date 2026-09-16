package br.com.fiap.campusride.controller;

import br.com.fiap.campusride.dto.CaronaRequest;
import br.com.fiap.campusride.dto.CaronaDetalheResponse;
import br.com.fiap.campusride.dto.CaronaResponse;
import br.com.fiap.campusride.service.CaronaService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caronas")
public class CaronaController {

    private final CaronaService caronaService;

    public CaronaController(CaronaService caronaService) {
        this.caronaService = caronaService;
    }

    @PostMapping
    public ResponseEntity<CaronaResponse> criar(@RequestBody CaronaRequest request) {
        CaronaResponse response = caronaService.criar(request);
        URI location = URI.create("/caronas/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CaronaResponse>> listar() {
        return ResponseEntity.ok(caronaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaronaDetalheResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caronaService.buscarPorId(id));
    }
}
