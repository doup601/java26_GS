package br.com.fiap3esph.trajetoria.application.core.specification;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.application.core.specification.contract.ValidadorPedido;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorLimitePecasPedido implements ValidadorPedido {
    private static final int LIMITE_PECAS = 5;

    @Override
    public void validar(DadosPedido dados) {
        if (dados.itens().size() > LIMITE_PECAS) {
            throw new ValidacaoException("Um pedido pode conter no máximo " + LIMITE_PECAS + " peças!");
        }
    }
}
