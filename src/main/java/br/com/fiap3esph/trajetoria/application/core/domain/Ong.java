package br.com.fiap3esph.trajetoria.application.core.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ong {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private boolean ativo;

    public void atualizarInformacoes(String nome, String email, String telefone) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (telefone != null && !telefone.isBlank()) {
            this.telefone = telefone;
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
