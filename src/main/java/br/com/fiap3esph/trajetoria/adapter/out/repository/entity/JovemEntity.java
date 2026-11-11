package br.com.fiap3esph.trajetoria.adapter.out.repository.entity;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Jovem")
@Table(name = "jovens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class JovemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Tamanho tamanho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    private OngEntity ong;

    @Embedded
    private Endereco endereco;

    private boolean consentimentoAceito;
    private LocalDateTime dataConsentimento;
    private boolean validado;
    private boolean ativo;
}
