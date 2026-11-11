package br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosDetalhamentoPeca(
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
        String origem,
        StatusPeca status,
        @JsonProperty("motivo_descarte")
        String motivoDescarte) {
}
