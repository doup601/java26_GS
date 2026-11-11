package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DadosItemFeedback(
        @NotNull
        @JsonProperty("id_item")
        Long idItem,

        @NotNull
        Boolean serviu) {
}
