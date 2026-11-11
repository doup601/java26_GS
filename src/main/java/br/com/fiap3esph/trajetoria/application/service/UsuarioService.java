package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.adapter.in.controller.mapper.UsuarioMapper;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.usuario.DadosListagemUsuario;
import br.com.fiap3esph.trajetoria.application.core.domain.Usuario;
import br.com.fiap3esph.trajetoria.application.port.out.UsuarioRepository;
import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.exception.type.UsuarioNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        Usuario usuario = new Usuario(
                null,
                dados.login(),
                passwordEncoder.encode(dados.senha()),
                dados.perfil()
        );
        Usuario saved = repository.save(usuario);
        return mapper.toDetails(saved);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoUsuario detalharUsuario(Long id) {
        Usuario usuario = repository
                .findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("ID do usuário informado não existe!"));
        return mapper.toDetails(usuario);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarUsuario(DadosAtualizacaoUsuario dados) {
        Usuario usuario = repository
                .findById(dados.id())
                .orElseThrow(() ->
                        new UsuarioNotFoundException("ID do usuário informado não existe!"));
        usuario.atualizarInformacoes(dados.login(), dados.perfil());
        Usuario saved = repository.save(usuario);
        return mapper.toDetails(saved);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        Usuario usuario = repository
                .findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("ID do usuário informado não existe!"));
        repository.delete(usuario);
    }

    @Transactional
    public void alterarSenha(String login, String senhaAtual, String novaSenha) {
        Usuario usuario = (Usuario) repository.findByLogin(login);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado!");
        }
        if (!passwordEncoder.matches(senhaAtual, usuario.getPassword())) {
            throw new ValidacaoException("Senha atual incorreta!");
        }
        usuario.alterarSenha(passwordEncoder.encode(novaSenha));
        repository.save(usuario);
    }
}
