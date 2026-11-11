package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.adapter.in.controller.mapper.JovemMapper;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem.DadosAtualizacaoJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem.DadosCadastroJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosDetalhamentoJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosListagemJovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Ong;
import br.com.fiap3esph.trajetoria.application.port.out.JovemRepository;
import br.com.fiap3esph.trajetoria.application.port.out.OngRepository;
import br.com.fiap3esph.trajetoria.exception.type.JovemNotFoundException;
import br.com.fiap3esph.trajetoria.exception.type.OngNotFoundException;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.mapper.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JovemService {
    private final JovemRepository repository;
    private final OngRepository ongRepository;
    private final JovemMapper mapper;
    private final EnderecoMapper enderecoMapper;
    private final EnderecoService enderecoService;

    @Transactional
    public DadosDetalhamentoJovem cadastrarJovem(DadosCadastroJovem dados) {
        Ong ong = ongRepository.buscarPorId(dados.idOng())
                .orElseThrow(() -> new OngNotFoundException("ONG informada não existe!"));
        Endereco endereco = enderecoService.enriquecer(enderecoMapper.toVo(dados.endereco()));
        Jovem jovem = new Jovem(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.tamanho(),
                ong,
                endereco,
                false,
                null,
                false,
                true
        );
        if (Boolean.TRUE.equals(dados.consentimentoAceito())) {
            jovem.aceitarConsentimento();
        }
        return mapper.toDetails(repository.salvar(jovem));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemJovem> listarJovens(Long ongId, Pageable paginacao) {
        Page<Jovem> jovens = ongId == null
                ? repository.buscarTodos(paginacao)
                : repository.buscarPorOng(ongId, paginacao);
        return jovens.map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoJovem detalharJovem(Long id) {
        return mapper.toDetails(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoJovem atualizarJovem(DadosAtualizacaoJovem dados) {
        Jovem jovem = buscar(dados.id());
        jovem.atualizarInformacoes(dados.nome(), dados.email(), dados.telefone(), dados.tamanho());
        if (dados.endereco() != null) {
            Endereco endereco = enderecoService.enriquecer(enderecoMapper.toVo(dados.endereco()));
            jovem.atualizarEndereco(endereco);
        }
        return mapper.toDetails(repository.salvar(jovem));
    }

    @Transactional
    public DadosDetalhamentoJovem validarJovem(Long id) {
        Jovem jovem = buscar(id);
        jovem.validar();
        return mapper.toDetails(repository.salvar(jovem));
    }

    @Transactional
    public void excluirJovem(Long id) {
        Jovem jovem = buscar(id);
        jovem.excluir();
        repository.salvar(jovem);
    }

    private Jovem buscar(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new JovemNotFoundException("Jovem informado não existe!"));
    }
}
