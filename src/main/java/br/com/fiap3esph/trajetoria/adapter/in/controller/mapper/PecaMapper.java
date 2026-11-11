package br.com.fiap3esph.trajetoria.adapter.in.controller.mapper;

import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosDetalhamentoPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosListagemPeca;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import org.springframework.stereotype.Component;

@Component
public class PecaMapper {
    public DadosDetalhamentoPeca toDetails(Peca peca) {
        return new DadosDetalhamentoPeca(
                peca.getId(),
                peca.getNome(),
                peca.getCategoria(),
                peca.getTamanho(),
                peca.getEstadoConservacao(),
                peca.getFotoUrl(),
                peca.getLocalArmazem(),
                peca.getOrigem(),
                peca.getStatus(),
                peca.getMotivoDescarte()
        );
    }

    public DadosListagemPeca toList(Peca peca) {
        return new DadosListagemPeca(
                peca.getId(),
                peca.getNome(),
                peca.getCategoria(),
                peca.getTamanho(),
                peca.getEstadoConservacao(),
                peca.getStatus()
        );
    }
}
