package br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosListagemPeca(
        Long id,
        String nome,
        CategoriaPeca categoria,
        Tamanho tamanho,
        @JsonProperty("estado_conservacao")
        EstadoConservacao estadoConservacao,
        StatusPeca status) {
}
