package br.com.fiap3esph.trajetoria.application.core.specification;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosItemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.core.specification.contract.ValidadorPedido;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.exception.type.PecaNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidadorPecasDisponiveis implements ValidadorPedido {
    private final PecaRepository pecaRepository;

    @Override
    public void validar(DadosPedido dados) {
        Set<Long> idsPecas = new HashSet<>();
        for (DadosItemPedido item : dados.itens()) {
            if (!idsPecas.add(item.idPeca())) {
                throw new ValidacaoException("A peça " + item.idPeca() + " foi informada mais de uma vez no pedido!");
            }
            Peca peca = pecaRepository.buscarPorId(item.idPeca())
                    .orElseThrow(() -> new PecaNotFoundException("Peça " + item.idPeca() + " não existe!"));
            if (peca.getStatus() != StatusPeca.DISPONIVEL) {
                throw new ValidacaoException("Peça " + peca.getId() + " não está disponível!");
            }
        }
    }
}
