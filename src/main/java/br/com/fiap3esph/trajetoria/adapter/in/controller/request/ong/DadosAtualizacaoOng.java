package br.com.fiap3esph.trajetoria.adapter.in.controller.request.ong;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoOng(
        @NotNull
        @JsonProperty("id")
        Long id,
        String nome,
        @Email
        String email,
        String telefone) {
}
