package br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido;

import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPedido;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DadosListagemPedido(
        Long id,
        @JsonProperty("nome_jovem")
        String nomeJovem,
        StatusPedido status,
        @JsonProperty("data_solicitacao")
        LocalDateTime dataSolicitacao) {
    public DadosListagemPedido(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getJovem() == null ? null : pedido.getJovem().getNome(),
                pedido.getStatus(),
                pedido.getDataSolicitacao()
        );
    }
}
