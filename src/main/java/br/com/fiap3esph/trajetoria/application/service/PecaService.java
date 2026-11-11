package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.adapter.in.controller.mapper.PecaMapper;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosAtualizacaoPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosCadastroPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosDescarte;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosDetalhamentoPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosListagemPeca;
import br.com.fiap3esph.trajetoria.application.core.domain.Peca;
import br.com.fiap3esph.trajetoria.application.port.out.PecaRepository;
import br.com.fiap3esph.trajetoria.exception.type.PecaNotFoundException;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PecaService {
    private final PecaRepository repository;
    private final PecaMapper mapper;

    @Transactional
    public DadosDetalhamentoPeca cadastrarPeca(DadosCadastroPeca dados) {
        Peca peca = new Peca(
                null,
                dados.nome(),
                dados.categoria(),
                dados.tamanho(),
                dados.estadoConservacao(),
                dados.fotoUrl(),
                dados.localArmazem(),
                dados.origem(),
                StatusPeca.DISPONIVEL,
                null
        );
        return mapper.toDetails(repository.salvar(peca));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemPeca> listarPecas(
            StatusPeca status,
            CategoriaPeca categoria,
            Tamanho tamanho,
            Pageable paginacao) {
        return repository
                .buscarComFiltros(status, categoria, tamanho, paginacao)
                .map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoPeca detalharPeca(Long id) {
        return mapper.toDetails(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoPeca atualizarPeca(DadosAtualizacaoPeca dados) {
        Peca peca = buscar(dados.id());
        peca.atualizarInformacoes(
                dados.nome(),
                dados.categoria(),
                dados.tamanho(),
                dados.estadoConservacao(),
                dados.fotoUrl(),
                dados.localArmazem(),
                dados.origem()
        );
        return mapper.toDetails(repository.salvar(peca));
    }

    @Transactional
    public DadosDetalhamentoPeca descartarPeca(Long id, DadosDescarte dados) {
        Peca peca = buscar(id);
        peca.descartar(dados.motivoDescarte());
        return mapper.toDetails(repository.salvar(peca));
    }

    @Transactional
    public void excluirPeca(Long id) {
        Peca peca = buscar(id);
        peca.descartar("Removida do acervo pela operação");
        repository.salvar(peca);
    }

    private Peca buscar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new PecaNotFoundException("Peça informada não existe!"));
    }
}
