package br.com.fiap3esph.trajetoria.application.core.specification;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosItemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.core.specification.contract.ValidadorPedido;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.exception.type.JovemNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.PecaNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorTamanhoPeca implements ValidadorPedido {
    private final JovemRepository jovemRepository;
    private final PecaRepository pecaRepository;

    @Override
    public void validar(DadosPedido dados) {
        Jovem jovem = jovemRepository.buscarPorId(dados.idJovem())
                .orElseThrow(() -> new JovemNotFoundException("Jovem informado não existe!"));
        for (DadosItemPedido item : dados.itens()) {
            Peca peca = pecaRepository.buscarPorId(item.idPeca())
                    .orElseThrow(() -> new PecaNotFoundException("Peça " + item.idPeca() + " não existe!"));
            if (peca.getTamanho() != jovem.getTamanho()) {
                throw new ValidacaoException(
                        "A peça " + peca.getId() + " (tamanho " + peca.getTamanho()
                                + ") não corresponde ao tamanho do jovem (" + jovem.getTamanho() + ")!");
            }
        }
    }
}
