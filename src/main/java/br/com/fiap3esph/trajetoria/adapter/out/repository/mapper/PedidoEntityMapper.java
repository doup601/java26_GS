package br.com.fiap3esph.trajetoria.adapter.out.repository.mapper;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.ItemPedidoEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.JovemEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PedidoEntity;
import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PedidoEntityMapper {
    private final JovemEntityMapper jovemEntityMapper;
    private final ItemPedidoEntityMapper itemPedidoEntityMapper;

    public PedidoEntity toEntity(Pedido pedido, JovemEntity jovemRef, Map<Long, PecaEntity> pecaRefs) {
        if (pedido == null) {
            return null;
        }
        PedidoEntity entity = new PedidoEntity(
                pedido.getId(),
                jovemRef,
                pedido.getDataSolicitacao(),
                pedido.getStatus(),
                new ArrayList<>(),
                pedido.getMotivoCancelamento(),
                pedido.getDataCancelamento(),
                pedido.getEntregador(),
                pedido.getCustoEntrega(),
                pedido.getDataDespacho(),
                pedido.getFoiEntrevista(),
                pedido.getComentarioFeedback(),
                pedido.getDataFeedback()
        );
        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                PecaEntity pecaRef = pecaRefs.get(item.getPeca().getId());
                ItemPedidoEntity itemEntity = itemPedidoEntityMapper.toEntity(item, pecaRef);
                itemEntity.setPedido(entity);
                entity.getItens().add(itemEntity);
            }
        }
        return entity;
    }

    public Pedido toDomain(PedidoEntity entity) {
        if (entity == null) {
            return null;
        }
        List<ItemPedido> itens = entity.getItens() == null
                ? List.of()
                : entity.getItens().stream().map(itemPedidoEntityMapper::toDomain).toList();
        return new Pedido(
                entity.getId(),
                jovemEntityMapper.toDomain(entity.getJovem()),
                entity.getDataSolicitacao(),
                entity.getStatus(),
                itens,
                entity.getMotivoCancelamento(),
                entity.getDataCancelamento(),
                entity.getEntregador(),
                entity.getCustoEntrega(),
                entity.getDataDespacho(),
                entity.getFoiEntrevista(),
                entity.getComentarioFeedback(),
                entity.getDataFeedback()
        );
    }
}
