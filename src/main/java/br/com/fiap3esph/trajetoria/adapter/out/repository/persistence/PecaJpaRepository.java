package br.com.fiap3esph.trajetoria.adapter.out.repository.persistence;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PecaJpaRepository extends JpaRepository<PecaEntity, Long> {
    @Query("""
            select p from Peca p
            where (:status is null or p.status = :status)
              and (:categoria is null or p.categoria = :categoria)
              and (:tamanho is null or p.tamanho = :tamanho)
            """)
    Page<PecaEntity> buscarComFiltros(
            @Param("status") StatusPeca status,
            @Param("categoria") CategoriaPeca categoria,
            @Param("tamanho") Tamanho tamanho,
            Pageable paginacao);
}
