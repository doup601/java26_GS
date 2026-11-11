package br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem;

import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;

public record DadosListagemJovem(
        Long id,
        String nome,
        String email,
        Tamanho tamanho) {
}
