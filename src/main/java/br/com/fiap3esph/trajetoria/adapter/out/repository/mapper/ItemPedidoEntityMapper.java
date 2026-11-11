package br.com.fiap3esph.trajetoria.adapter.out.repository.mapper;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.ItemPedidoEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemPedidoEntityMapper {
    private final PecaEntityMapper pecaEntityMapper;

    public ItemPedidoEntity toEntity(ItemPedido item, PecaEntity pecaRef) {
        if (item == null) {
            return null;
        }
        return new ItemPedidoEntity(
                item.getId(),
                pecaRef,
                item.getServiu(),
                null
        );
    }

    public ItemPedido toDomain(ItemPedidoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ItemPedido(
                entity.getId(),
                pecaEntityMapper.toDomain(entity.getPeca()),
                entity.getServiu()
        );
    }
}
