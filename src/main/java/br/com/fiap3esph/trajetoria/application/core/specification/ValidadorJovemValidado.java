package br.com.fiap3esph.trajetoria.application.core.specification;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.specification.contract.ValidadorPedido;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.exception.type.JovemNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorJovemValidado implements ValidadorPedido {
    private final JovemRepository jovemRepository;

    @Override
    public void validar(DadosPedido dados) {
        Jovem jovem = jovemRepository.buscarPorId(dados.idJovem())
                .orElseThrow(() -> new JovemNotFoundException("Jovem informado não existe!"));
        if (!jovem.isValidado()) {
            throw new ValidacaoException("Jovem ainda não foi validado pela ONG!");
        }
    }
}
