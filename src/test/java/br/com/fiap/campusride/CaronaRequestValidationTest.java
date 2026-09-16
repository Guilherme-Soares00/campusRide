package br.com.fiap.campusride;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.campusride.dto.CaronaRequest;
import br.com.fiap.campusride.model.TipoVeiculo;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CaronaRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsIncompatibleVehicleCapacity() {
        CaronaRequest request = new CaronaRequest(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.MOTO,
                2
        );

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("vagasTotais");
    }

    @Test
    void acceptsCompatibleVehicleCapacity() {
        CaronaRequest request = new CaronaRequest(
                "Ana Souza",
                "Campus Norte",
                "Campus Sul",
                LocalDateTime.now().plusDays(1),
                TipoVeiculo.CARRO,
                4
        );

        assertThat(validator.validate(request)).isEmpty();
    }
}
