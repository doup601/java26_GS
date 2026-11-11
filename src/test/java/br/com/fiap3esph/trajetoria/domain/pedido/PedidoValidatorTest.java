package br.com.fiap3esph.trajetoria.domain.pedido;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosItemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.core.specification.ValidadorJovemValidado;
import br.com.fiap3esph.trajetoria.application.core.specification.ValidadorLimitePecasPedido;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoValidatorTest {
    @Mock
    JovemRepository jovemRepository;

    @InjectMocks
    ValidadorJovemValidado validadorJovemValidado;

    private final ValidadorLimitePecasPedido validadorLimite = new ValidadorLimitePecasPedido();

    @Test
    @DisplayName("Expectativa: Pedido de jovem não validado é recusado.")
    void recusarJovemNaoValidado() {
        Jovem jovem = new Jovem(
                1L, "Maria", "maria@email.com", "11 97777-0000", Tamanho.M,
                new Ong(1L, "ONG", "ong@email.com", "11 99999-0000", true),
                new Endereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01000-000"),
                true, null, false, true);
        when(jovemRepository.buscarPorId(1L)).thenReturn(Optional.of(jovem));

        DadosPedido dados = new DadosPedido(1L, List.of(new DadosItemPedido(1L)));

        assertThatThrownBy(() -> validadorJovemValidado.validar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("validado");
    }

    @Test
    @DisplayName("Expectativa: Pedido com mais de 5 peças é recusado.")
    void recusarPedidoAcimaDoLimite() {
        DadosPedido dados = new DadosPedido(1L, List.of(
                new DadosItemPedido(1L),
                new DadosItemPedido(2L),
                new DadosItemPedido(3L),
                new DadosItemPedido(4L),
                new DadosItemPedido(5L),
                new DadosItemPedido(6L)));

        assertThatThrownBy(() -> validadorLimite.validar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("máximo");
    }
}
