package br.com.fiap3esph.trajetoria.adapter.in.controller.response.usuario;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Role;

public record DadosListagemUsuario(
        Long id,
        String login,
        Role perfil) {
}
