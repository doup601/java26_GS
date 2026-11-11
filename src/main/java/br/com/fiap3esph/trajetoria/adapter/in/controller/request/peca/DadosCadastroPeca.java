package br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroPeca(
        @NotBlank
        String nome,

        @NotNull
        CategoriaPeca categoria,

        @NotNull
        Tamanho tamanho,

        @NotNull
        @JsonProperty("estado_conservacao")
        EstadoConservacao estadoConservacao,

        @NotBlank
        @JsonProperty("foto_url")
        String fotoUrl,

        @JsonProperty("local_armazem")
        String localArmazem,

        String origem) {
}
