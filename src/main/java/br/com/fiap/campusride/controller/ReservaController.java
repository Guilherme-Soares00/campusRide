package br.com.fiap.campusride.controller;

import br.com.fiap.campusride.dto.ReservaRequest;
import br.com.fiap.campusride.dto.ReservaResponse;
import br.com.fiap.campusride.service.ReservaService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/caronas/{caronaId}/reservas")
    public ResponseEntity<ReservaResponse> reservar(@PathVariable Long caronaId, @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.reservar(caronaId, request);
        URI location = URI.create("/reservas/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/reservas/{id}/cancelamento")
    public ResponseEntity<ReservaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelar(id));
    }
}
