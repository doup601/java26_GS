package br.com.fiap3esph.trajetoria.application.port.out;

import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JovemRepository {
    Jovem salvar(Jovem jovem);

    Optional<Jovem> buscarPorId(Long id);

    Page<Jovem> buscarTodos(Pageable paginacao);

    Page<Jovem> buscarPorOng(Long ongId, Pageable paginacao);
}
