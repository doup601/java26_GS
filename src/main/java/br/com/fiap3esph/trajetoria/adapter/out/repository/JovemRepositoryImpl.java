package br.com.fiap3esph.trajetoria.adapter.out.repository;

import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.JovemEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.entity.OngEntity;
import br.com.fiap3esph.trajetoria.adapter.out.repository.mapper.JovemEntityMapper;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.JovemJpaRepository;
import br.com.fiap3esph.trajetoria.adapter.out.repository.persistence.OngJpaRepository;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JovemRepositoryImpl implements JovemRepository {
    private final JovemJpaRepository jovemJpaRepository;
    private final OngJpaRepository ongJpaRepository;
    private final JovemEntityMapper mapper;

    @Override
    public Jovem salvar(Jovem jovem) {
        OngEntity ongRef = jovem.getOng() == null
                ? null
                : ongJpaRepository.getReferenceById(jovem.getOng().getId());
        JovemEntity saved = jovemJpaRepository.save(mapper.toEntity(jovem, ongRef));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Jovem> buscarPorId(Long id) {
        return jovemJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Jovem> buscarTodos(Pageable paginacao) {
        return jovemJpaRepository.findAllByAtivoTrue(paginacao).map(mapper::toDomain);
    }

    @Override
    public Page<Jovem> buscarPorOng(Long ongId, Pageable paginacao) {
        return jovemJpaRepository.findByOngIdAndAtivoTrue(ongId, paginacao).map(mapper::toDomain);
    }
}
