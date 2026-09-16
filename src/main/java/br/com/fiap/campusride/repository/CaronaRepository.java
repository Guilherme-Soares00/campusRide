package br.com.fiap.campusride.repository;

import br.com.fiap.campusride.model.Carona;
import br.com.fiap.campusride.model.SituacaoCarona;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CaronaRepository extends JpaRepository<Carona, Long> {

    List<Carona> findBySituacaoAndDataHoraPartidaAfterOrderByDataHoraPartidaAsc(
            SituacaoCarona situacao,
            LocalDateTime dataHora
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select carona from Carona carona where carona.id = :id")
    Optional<Carona> findByIdForUpdate(@Param("id") Long id);
}
