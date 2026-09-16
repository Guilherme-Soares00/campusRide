package br.com.fiap.campusride.validation;

import br.com.fiap.campusride.dto.CaronaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CapacidadeVeiculoCompativelValidator implements ConstraintValidator<CapacidadeVeiculoCompativel, CaronaRequest> {

    @Override
    public boolean isValid(CaronaRequest request, ConstraintValidatorContext context) {
        if (request == null || request.tipoVeiculo() == null || request.vagasTotais() == null) {
            return true;
        }

        return switch (request.tipoVeiculo()) {
            case MOTO -> request.vagasTotais() <= 1;
            case CARRO -> request.vagasTotais() <= 5;
            case SUV -> request.vagasTotais() <= 7;
            case VAN -> request.vagasTotais() <= 15;
        };
    }
}
