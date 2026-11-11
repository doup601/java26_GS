package br.com.fiap3esph.trajetoria.application.core.domain;

import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.MotivoCancelamento;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {
    private Long id;
    private Jovem jovem;
    private LocalDateTime dataSolicitacao;
    private StatusPedido status;
    private List<ItemPedido> itens;
    private MotivoCancelamento motivoCancelamento;
    private LocalDateTime dataCancelamento;
    private String entregador;
    private BigDecimal custoEntrega;
    private LocalDateTime dataDespacho;
    private Boolean foiEntrevista;
    private String comentarioFeedback;
    private LocalDateTime dataFeedback;

    public static Pedido criar(Jovem jovem, List<ItemPedido> itens) {
        return new Pedido(
                null,
                jovem,
                LocalDateTime.now(),
                StatusPedido.SOLICITADO,
                itens,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public void aprovar() {
        exigirStatus(StatusPedido.SOLICITADO, "Pedido não está aguardando aprovação!");
        this.itens.forEach(item -> item.getPeca().separar());
        this.status = StatusPedido.APROVADO;
    }

    public void despachar(String entregador, BigDecimal custoEntrega) {
        exigirStatus(StatusPedido.APROVADO, "Pedido não está aprovado para despacho!");
        if (entregador == null || entregador.isBlank()) {
            throw new ValidacaoException("Nome do entregador é obrigatório!");
        }
        if (custoEntrega == null || custoEntrega.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacaoException("Custo de entrega inválido!");
        }
        if (custoEntrega.compareTo(new BigDecimal("50.00")) > 0) {
            throw new ValidacaoException("Custo de entrega não pode exceder R$ 50,00!");
        }
        this.itens.forEach(item -> item.getPeca().despachar());
        this.entregador = entregador;
        this.custoEntrega = custoEntrega;
        this.dataDespacho = LocalDateTime.now();
        this.status = StatusPedido.DESPACHADO;
    }

    public void confirmarEntrega() {
        exigirStatus(StatusPedido.DESPACHADO, "Pedido não está em trânsito para confirmação de entrega!");
        this.itens.forEach(item -> item.getPeca().confirmarEntrega());
        this.status = StatusPedido.ENTREGUE;
    }

    public void cancelar(MotivoCancelamento motivo) {
        if (this.status == StatusPedido.DESPACHADO || this.status == StatusPedido.ENTREGUE) {
            throw new ValidacaoException("Pedido já despachado não pode ser cancelado!");
        }
        if (this.status == StatusPedido.CANCELADO) {
            throw new ValidacaoException("Pedido já está cancelado!");
        }
        if (motivo == null) {
            throw new ValidacaoException("Motivo do cancelamento é obrigatório!");
        }
        this.itens.forEach(item -> item.getPeca().devolverAoEstoque());
        this.motivoCancelamento = motivo;
        this.dataCancelamento = LocalDateTime.now();
        this.status = StatusPedido.CANCELADO;
    }

    public void registrarFeedback(Map<Long, Boolean> serviuPorItem, Boolean foiEntrevista, String comentario) {
        exigirStatus(StatusPedido.ENTREGUE, "Feedback só pode ser registrado após a entrega!");
        this.itens.forEach(item -> {
            if (serviuPorItem.containsKey(item.getId())) {
                Boolean serviu = serviuPorItem.get(item.getId());
                item.registrarFeedback(serviu);
                if (Boolean.FALSE.equals(serviu)) {
                    item.getPeca().devolverAoEstoque();
                }
            }
        });
        this.foiEntrevista = foiEntrevista;
        this.comentarioFeedback = comentario;
        this.dataFeedback = LocalDateTime.now();
    }

    private void exigirStatus(StatusPedido esperado, String mensagem) {
        if (this.status != esperado) {
            throw new ValidacaoException(mensagem);
        }
    }
}
