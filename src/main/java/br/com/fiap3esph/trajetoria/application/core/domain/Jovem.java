package br.com.fiap3esph.trajetoria.application.core.domain;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Jovem {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Tamanho tamanho;
    private Ong ong;
    private Endereco endereco;
    private boolean consentimentoAceito;
    private LocalDateTime dataConsentimento;
    private boolean validado;
    private boolean ativo;

    public void atualizarInformacoes(String nome, String email, String telefone, Tamanho tamanho) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (telefone != null && !telefone.isBlank()) {
            this.telefone = telefone;
        }
        if (tamanho != null) {
            this.tamanho = tamanho;
        }
    }

    public void atualizarEndereco(Endereco endereco) {
        if (endereco != null) {
            this.endereco = endereco;
        }
    }

    public void aceitarConsentimento() {
        this.consentimentoAceito = true;
        this.dataConsentimento = LocalDateTime.now();
    }

    public void validar() {
        this.validado = true;
    }

    public void excluir() {
        this.ativo = false;
    }
}
