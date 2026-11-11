package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosPedido(
        @NotNull
        @JsonProperty("id_jovem")
        Long idJovem,

        @NotEmpty
        @Valid
        List<DadosItemPedido> itens) {
}
