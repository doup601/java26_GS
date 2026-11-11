package br.com.fiap3esph.trajetoria.application.port.out;

import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OngRepository {
    Ong salvar(Ong ong);

    Optional<Ong> buscarPorId(Long id);

    Page<Ong> buscarTodos(Pageable paginacao);
}
