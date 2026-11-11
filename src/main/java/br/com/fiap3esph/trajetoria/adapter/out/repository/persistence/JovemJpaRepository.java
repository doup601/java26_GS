package br.com.fiap3esph.trajetoria.adapter.out.repository.persistence;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.JovemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JovemJpaRepository extends JpaRepository<JovemEntity, Long> {
    Page<JovemEntity> findAllByAtivoTrue(Pageable paginacao);

    Page<JovemEntity> findByOngIdAndAtivoTrue(Long ongId, Pageable paginacao);
}
