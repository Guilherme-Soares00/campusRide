package br.com.fiap.campusride.repository;

import br.com.fiap.campusride.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
