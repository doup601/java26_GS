package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosCancelamento;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosDespacho;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosFeedback;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosItemFeedback;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosItemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido.DadosListagemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido.DetalhamentoPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import br.com.fiap3esph.trajetoria.application.core.specification.contract.ValidadorPedido;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.application.port.out.PedidoRepository;
import br.com.fiap3esph.trajetoria.exception.type.JovemNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.PecaNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.PedidoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final JovemRepository jovemRepository;
    private final PecaRepository pecaRepository;
    private final List<ValidadorPedido> validadores;

    @Transactional
    public DetalhamentoPedido criarPedido(DadosPedido dados) {
        validadores.forEach(validador -> validador.validar(dados));
        Jovem jovem = jovemRepository.buscarPorId(dados.idJovem())
                .orElseThrow(() -> new JovemNotFoundException("Jovem informado não existe!"));
        List<ItemPedido> itens = dados.itens().stream()
                .map(this::criarItem)
                .toList();
        Pedido pedido = Pedido.criar(jovem, itens);
        itens.forEach(item -> item.getPeca().reservar());
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemPedido> listarPedidos(Long ongId, Pageable paginacao) {
        Page<Pedido> pedidos = ongId == null
                ? pedidoRepository.buscarTodos(paginacao)
                : pedidoRepository.buscarPorOng(ongId, paginacao);
        return pedidos.map(DadosListagemPedido::new);
    }

    @Transactional(readOnly = true)
    public DetalhamentoPedido detalharPedido(Long id) {
        return new DetalhamentoPedido(buscar(id));
    }

    @Transactional
    public DetalhamentoPedido aprovarPedido(Long id) {
        Pedido pedido = buscar(id);
        pedido.aprovar();
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    @Transactional
    public DetalhamentoPedido despacharPedido(Long id, DadosDespacho dados) {
        Pedido pedido = buscar(id);
        pedido.despachar(dados.entregador(), dados.custoEntrega());
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    @Transactional
    public DetalhamentoPedido confirmarEntrega(Long id) {
        Pedido pedido = buscar(id);
        pedido.confirmarEntrega();
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    @Transactional
    public DetalhamentoPedido cancelarPedido(Long id, DadosCancelamento dados) {
        Pedido pedido = buscar(id);
        pedido.cancelar(dados.motivo());
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    @Transactional
    public DetalhamentoPedido registrarFeedback(Long id, DadosFeedback dados) {
        Pedido pedido = buscar(id);
        Map<Long, Boolean> serviuPorItem = dados.itens().stream()
                .collect(Collectors.toMap(
                        DadosItemFeedback::idItem,
                        DadosItemFeedback::serviu,
                        (primeiro, segundo) -> primeiro));
        pedido.registrarFeedback(serviuPorItem, dados.foiEntrevista(), dados.comentario());
        salvarPecas(pedido);
        return new DetalhamentoPedido(pedidoRepository.salvar(pedido));
    }

    private ItemPedido criarItem(DadosItemPedido dados) {
        Peca peca = pecaRepository.buscarPorId(dados.idPeca())
                .orElseThrow(() -> new PecaNotFoundException("Peça " + dados.idPeca() + " não existe!"));
        return new ItemPedido(null, peca, null);
    }

    private void salvarPecas(Pedido pedido) {
        pedido.getItens().stream()
                .map(ItemPedido::getPeca)
                .distinct()
                .forEach(pecaRepository::salvar);
    }

    private Pedido buscar(Long id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido informado não existe!"));
    }
}
