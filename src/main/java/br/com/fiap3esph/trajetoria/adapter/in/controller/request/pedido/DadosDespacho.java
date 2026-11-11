package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record DadosDespacho(
        @NotBlank
        String entregador,

        @NotNull
        @PositiveOrZero
        @DecimalMax("50.00")
        @JsonProperty("custo_entrega")
        BigDecimal custoEntrega) {
}
