package br.com.fiap.campusride.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motorista;
    private String origem;
    private String destino;
    private LocalDateTime dataHoraPartida;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;

    private Integer vagasTotais;

    @Enumerated(EnumType.STRING)
    private SituacaoCarona situacao;

    @OneToMany(mappedBy = "carona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public Carona(String motorista, String origem, String destino, LocalDateTime dataHoraPartida, TipoVeiculo tipoVeiculo, Integer vagasTotais) {
        this.motorista = motorista;
        this.origem = origem;
        this.destino = destino;
        this.dataHoraPartida = dataHoraPartida;
        this.tipoVeiculo = tipoVeiculo;
        this.vagasTotais = vagasTotais;
        this.situacao = SituacaoCarona.ABERTA;
    }
}
