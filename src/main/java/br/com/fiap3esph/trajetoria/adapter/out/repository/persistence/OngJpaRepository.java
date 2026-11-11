package br.com.fiap3esph.trajetoria.adapter.out.repository.persistence;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OngJpaRepository extends JpaRepository<OngEntity, Long> {
    Page<OngEntity> findAllByAtivoTrue(Pageable paginacao);
}
