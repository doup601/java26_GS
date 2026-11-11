package br.com.fiap3esph.trajetoria.adapter.in.controller.request.usuario;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotBlank
        String login,

        @NotBlank
        String senha,

        @NotNull
        Role perfil) {
}
