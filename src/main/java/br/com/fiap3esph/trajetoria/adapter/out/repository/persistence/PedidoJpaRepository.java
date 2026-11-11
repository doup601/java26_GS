package br.com.fiap3esph.trajetoria.adapter.out.repository.persistence;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
    Page<PedidoEntity> findByJovemOngId(Long ongId, Pageable paginacao);
}
