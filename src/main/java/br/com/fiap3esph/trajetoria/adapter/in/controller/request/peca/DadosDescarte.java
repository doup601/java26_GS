package br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record DadosDescarte(
        @NotBlank
        @JsonProperty("motivo_descarte")
        String motivoDescarte) {
}
