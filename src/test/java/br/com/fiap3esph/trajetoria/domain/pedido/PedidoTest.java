package br.com.fiap3esph.trajetoria.domain.pedido;

import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.MotivoCancelamento;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPedido;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PedidoTest {
    private Peca criarPeca(Long id) {
        return new Peca(id, "Camisa social", CategoriaPeca.CAMISA, Tamanho.M,
                EstadoConservacao.BOM, "https://exemplo.com/camisa.jpg", "A1", "Doação",
                StatusPeca.DISPONIVEL, null);
    }

    private Jovem criarJovem() {
        return new Jovem(
                1L, "Maria", "maria@email.com", "11 97777-0000", Tamanho.M,
                new Ong(1L, "ONG", "ong@email.com", "11 99999-0000", true),
                new Endereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01000-000"),
                true, null, true, true);
    }

    @Test
    @DisplayName("Expectativa: Fluxo completo do pedido altera status do pedido e das peças.")
    void fluxoCompletoDoPedido() {
        //arrange or given
        Peca peca = criarPeca(1L);
        ItemPedido item = new ItemPedido(null, peca, null);
        Pedido pedido = Pedido.criar(criarJovem(), List.of(item));
        item.getPeca().reservar();

        //act or when
        pedido.aprovar();
        pedido.despachar("Motoboy Carlos", new BigDecimal("25.00"));
        pedido.confirmarEntrega();

        //assert or then
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.ENTREGUE);
        assertThat(peca.getStatus()).isEqualTo(StatusPeca.ENTREGUE);
        assertThat(pedido.getEntregador()).isEqualTo("Motoboy Carlos");
        assertThat(pedido.getCustoEntrega()).isEqualByComparingTo("25.00");
    }

    @Test
    @DisplayName("Expectativa: Cancelar devolve as peças ao acervo.")
    void cancelarDevolvePecasAoEstoque() {
        //arrange or given
        Peca peca = criarPeca(1L);
        ItemPedido item = new ItemPedido(null, peca, null);
        Pedido pedido = Pedido.criar(criarJovem(), List.of(item));
        peca.reservar();

        //act or when
        pedido.cancelar(MotivoCancelamento.JOVEM_DESISTIU);

        //assert or then
        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(pedido.getMotivoCancelamento()).isEqualTo(MotivoCancelamento.JOVEM_DESISTIU);
        assertThat(peca.getStatus()).isEqualTo(StatusPeca.DISPONIVEL);
    }

    @Test
    @DisplayName("Expectativa: Pedido despachado não pode ser cancelado.")
    void naoCancelarPedidoDespachado() {
        //arrange or given
        Peca peca = criarPeca(1L);
        ItemPedido item = new ItemPedido(null, peca, null);
        Pedido pedido = Pedido.criar(criarJovem(), List.of(item));
        peca.reservar();
        pedido.aprovar();
        pedido.despachar("Motoboy Carlos", new BigDecimal("25.00"));

        //act or when
        //assert or then
        assertThatThrownBy(() -> pedido.cancelar(MotivoCancelamento.OUTROS))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    @DisplayName("Expectativa: Feedback com peça que não serviu devolve a peça ao acervo.")
    void feedbackDevolvePecaQueNaoServiu() {
        //arrange or given
        Peca peca = criarPeca(1L);
        ItemPedido item = new ItemPedido(10L, peca, null);
        Pedido pedido = Pedido.criar(criarJovem(), List.of(item));
        peca.reservar();
        pedido.aprovar();
        pedido.despachar("Motoboy Carlos", new BigDecimal("25.00"));
        pedido.confirmarEntrega();

        //act or when
        pedido.registrarFeedback(Map.of(10L, false), true, "A camisa ficou grande");

        //assert or then
        assertThat(item.getServiu()).isFalse();
        assertThat(peca.getStatus()).isEqualTo(StatusPeca.DISPONIVEL);
        assertThat(pedido.getFoiEntrevista()).isTrue();
        assertThat(pedido.getComentarioFeedback()).isEqualTo("A camisa ficou grande");
        assertThat(pedido.getDataFeedback()).isNotNull();
    }
}
