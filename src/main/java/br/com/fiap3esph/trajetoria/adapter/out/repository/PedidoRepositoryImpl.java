package br.com.fiap3esph.trajetoria.adapter.out.repository;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.JovemEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PedidoEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.PedidoEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.JovemJpaRepository;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.PecaJpaRepository;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.PedidoJpaRepository;
import br.com.fiap3esph.trajetoria.application.core.domain.ItemPedido;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.core.domain.Pedido;
import br.com.fiap3esph.trajetoria.application.port.out.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {
    private final PedidoJpaRepository pedidoJpaRepository;
    private final JovemJpaRepository jovemJpaRepository;
    private final PecaJpaRepository pecaJpaRepository;
    private final PedidoEntityMapper mapper;

    @Override
    public Pedido salvar(Pedido pedido) {
        JovemEntity jovemRef = pedido.getJovem() == null
                ? null
                : jovemJpaRepository.getReferenceById(pedido.getJovem().getId());
        Map<Long, PecaEntity> pecaRefs = pedido.getItens() == null
                ? Map.of()
                : pedido.getItens().stream()
                        .map(ItemPedido::getPeca)
                        .collect(Collectors.toMap(
                                Peca::getId,
                                peca -> pecaJpaRepository.getReferenceById(peca.getId()),
                                (primeira, segunda) -> primeira));
        PedidoEntity entity = mapper.toEntity(pedido, jovemRef, pecaRefs);
        PedidoEntity saved = pedidoJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Pedido> buscarTodos(Pageable paginacao) {
        return pedidoJpaRepository.findAll(paginacao).map(mapper::toDomain);
    }

    @Override
    public Page<Pedido> buscarPorOng(Long ongId, Pageable paginacao) {
        return pedidoJpaRepository.findByJovemOngId(ongId, paginacao).map(mapper::toDomain);
    }
}
