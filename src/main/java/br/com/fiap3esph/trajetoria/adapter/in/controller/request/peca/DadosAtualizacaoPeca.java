package br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPeca(
        @NotNull
        @JsonProperty("id")
        Long id,
        String nome,
        CategoriaPeca categoria,
        Tamanho tamanho,
        @JsonProperty("estado_conservacao")
        EstadoConservacao estadoConservacao,
        @JsonProperty("foto_url")
        String fotoUrl,
        @JsonProperty("local_armazem")
        String localArmazem,
        String origem) {
}
