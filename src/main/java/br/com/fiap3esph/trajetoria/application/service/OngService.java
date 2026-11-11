package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.adapter.in.controller.mapper.OngMapper;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.ong.DadosAtualizacaoOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.ong.DadosCadastroOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosDetalhamentoOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosListagemOng;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.port.out.OngRepository;
import br.com.fiap3esph.trajetoria.exception.type.OngNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OngService {
    private final OngRepository repository;
    private final OngMapper mapper;

    @Transactional
    public DadosDetalhamentoOng cadastrarOng(DadosCadastroOng dados) {
        Ong ong = new Ong(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                true
        );
        return mapper.toDetails(repository.salvar(ong));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemOng> listarOngs(Pageable paginacao) {
        return repository.buscarTodos(paginacao).map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoOng detalharOng(Long id) {
        return mapper.toDetails(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoOng atualizarOng(DadosAtualizacaoOng dados) {
        Ong ong = buscar(dados.id());
        ong.atualizarInformacoes(dados.nome(), dados.email(), dados.telefone());
        return mapper.toDetails(repository.salvar(ong));
    }

    @Transactional
    public void excluirOng(Long id) {
        Ong ong = buscar(id);
        ong.excluir();
        repository.salvar(ong);
    }

    private Ong buscar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new OngNotFoundException("ONG informada não existe!"));
    }
}
