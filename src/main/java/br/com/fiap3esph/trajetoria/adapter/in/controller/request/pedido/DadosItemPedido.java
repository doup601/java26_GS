package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DadosItemPedido(
        @NotNull
        @JsonProperty("id_peca")
        Long idPeca) {
}
