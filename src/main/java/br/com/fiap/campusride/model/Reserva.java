package br.com.fiap.campusride.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carona_id", nullable = false)
    private Carona carona;

    private String passageiro;
    private LocalDateTime realizadaEm;

    @Enumerated(EnumType.STRING)
    private SituacaoReserva situacao;

    public Reserva(Carona carona, String passageiro) {
        this.carona = carona;
        this.passageiro = passageiro;
        this.realizadaEm = LocalDateTime.now();
        this.situacao = SituacaoReserva.CONFIRMADA;
    }

    public void cancelar() {
        this.situacao = SituacaoReserva.CANCELADA;
    }

    public boolean estaCancelada() {
        return situacao == SituacaoReserva.CANCELADA;
    }
}
