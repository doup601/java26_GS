package br.com.fiap3esph.trajetoria.application.port.out;

import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PecaRepository {
    Peca salvar(Peca peca);

    Optional<Peca> buscarPorId(Long id);

    Page<Peca> buscarComFiltros(
            StatusPeca status,
            CategoriaPeca categoria,
            Tamanho tamanho,
            Pageable paginacao);
}
