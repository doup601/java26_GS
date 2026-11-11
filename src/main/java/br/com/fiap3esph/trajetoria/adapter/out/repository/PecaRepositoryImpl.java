package br.com.fiap3esph.trajetoria.adapter.out.repository;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.PecaEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.PecaEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.PecaJpaRepository;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PecaRepositoryImpl implements PecaRepository {
    private final PecaJpaRepository pecaJpaRepository;
    private final PecaEntityMapper mapper;

    @Override
    public Peca salvar(Peca peca) {
        PecaEntity saved = pecaJpaRepository.save(mapper.toEntity(peca));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Peca> buscarPorId(Long id) {
        return pecaJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Peca> buscarComFiltros(
            StatusPeca status,
            CategoriaPeca categoria,
            Tamanho tamanho,
            Pageable paginacao) {
        return pecaJpaRepository
                .buscarComFiltros(status, categoria, tamanho, paginacao)
                .map(mapper::toDomain);
    }
}
