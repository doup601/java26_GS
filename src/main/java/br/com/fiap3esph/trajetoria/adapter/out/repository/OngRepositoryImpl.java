package br.com.fiap3esph.trajetoria.adapter.out.repository;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.OngEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.OngJpaRepository;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.port.out.OngRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OngRepositoryImpl implements OngRepository {
    private final OngJpaRepository ongJpaRepository;
    private final OngEntityMapper mapper;

    @Override
    public Ong salvar(Ong ong) {
        OngEntity saved = ongJpaRepository.save(mapper.toEntity(ong));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Ong> buscarPorId(Long id) {
        return ongJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Ong> buscarTodos(Pageable paginacao) {
        return ongJpaRepository.findAllByAtivoTrue(paginacao).map(mapper::toDomain);
    }
}
