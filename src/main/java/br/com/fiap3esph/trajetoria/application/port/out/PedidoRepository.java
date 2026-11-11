package br.com.fiap3esph.trajetoria.application.port.out;

import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    Page<Pedido> buscarTodos(Pageable paginacao);

    Page<Pedido> buscarPorOng(Long ongId, Pageable paginacao);
}
