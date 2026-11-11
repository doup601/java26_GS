package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DadosFeedback(
        @NotEmpty
        @Valid
        List<DadosItemFeedback> itens,

        @JsonProperty("foi_entrevista")
        Boolean foiEntrevista,

        String comentario) {
}
