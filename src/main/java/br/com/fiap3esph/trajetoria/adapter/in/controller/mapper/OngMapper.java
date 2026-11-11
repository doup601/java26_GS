package br.com.fiap3esph.trajetoria.adapter.in.controller.mapper;

import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosDetalhamentoOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosListagemOng;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import org.springframework.stereotype.Component;

@Component
public class OngMapper {
    public DadosDetalhamentoOng toDetails(Ong ong) {
        return new DadosDetalhamentoOng(
                ong.getId(),
                ong.getNome(),
                ong.getEmail(),
                ong.getTelefone(),
                ong.isAtivo()
        );
    }

    public DadosListagemOng toList(Ong ong) {
        return new DadosListagemOng(
                ong.getId(),
                ong.getNome(),
                ong.getEmail()
        );
    }
}
