package br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamento(
        @NotNull
        MotivoCancelamento motivo) {
}
