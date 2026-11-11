package br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido;

import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.MotivoCancelamento;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPedido;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalhamentoPedido(
        Long id,
        @JsonProperty("id_jovem")
        Long idJovem,
        @JsonProperty("nome_jovem")
        String nomeJovem,
        StatusPedido status,
        @JsonProperty("data_solicitacao")
        LocalDateTime dataSolicitacao,
        List<DadosItemPedidoDetalhe> itens,
        String entregador,
        @JsonProperty("custo_entrega")
        BigDecimal custoEntrega,
        @JsonProperty("data_despacho")
        LocalDateTime dataDespacho,
        @JsonProperty("motivo_cancelamento")
        MotivoCancelamento motivoCancelamento,
        @JsonProperty("data_cancelamento")
        LocalDateTime dataCancelamento,
        @JsonProperty("foi_entrevista")
        Boolean foiEntrevista,
        @JsonProperty("comentario_feedback")
        String comentarioFeedback,
        @JsonProperty("data_feedback")
        LocalDateTime dataFeedback) {
    public DetalhamentoPedido(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getJovem() == null ? null : pedido.getJovem().getId(),
                pedido.getJovem() == null ? null : pedido.getJovem().getNome(),
                pedido.getStatus(),
                pedido.getDataSolicitacao(),
                pedido.getItens() == null
                        ? List.of()
                        : pedido.getItens().stream().map(DadosItemPedidoDetalhe::new).toList(),
                pedido.getEntregador(),
                pedido.getCustoEntrega(),
                pedido.getDataDespacho(),
                pedido.getMotivoCancelamento(),
                pedido.getDataCancelamento(),
                pedido.getFoiEntrevista(),
                pedido.getComentarioFeedback(),
                pedido.getDataFeedback()
        );
    }
}
