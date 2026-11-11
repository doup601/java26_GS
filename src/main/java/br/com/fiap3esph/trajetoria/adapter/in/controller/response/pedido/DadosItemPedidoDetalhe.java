package br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido;

import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosItemPedidoDetalhe(
        Long id,
        @JsonProperty("id_peca")
        Long idPeca,
        @JsonProperty("nome_peca")
        String nomePeca,
        CategoriaPeca categoria,
        Tamanho tamanho,
        StatusPeca status,
        Boolean serviu) {
    public DadosItemPedidoDetalhe(ItemPedido item) {
        this(
                item.getId(),
                item.getPeca() == null ? null : item.getPeca().getId(),
                item.getPeca() == null ? null : item.getPeca().getNome(),
                item.getPeca() == null ? null : item.getPeca().getCategoria(),
                item.getPeca() == null ? null : item.getPeca().getTamanho(),
                item.getPeca() == null ? null : item.getPeca().getStatus(),
                item.getServiu()
        );
    }
}
