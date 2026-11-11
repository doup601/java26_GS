package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.usuario.DadosAlteracaoSenha;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.usuario.DadosLogin;
import br.com.fiap3esph.trajetoria.application.core.domain.Usuario;
import br.com.fiap3esph.trajetoria.application.service.UsuarioService;
import br.com.fiap3esph.trajetoria.config.security.service.dto.DadosTokenJWT;
import br.com.fiap3esph.trajetoria.config.security.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosLogin dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(token);
        String tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

    @PutMapping("/senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> alterarSenha(
            @RequestBody @Valid DadosAlteracaoSenha dados,
            @AuthenticationPrincipal Usuario usuario) {
        usuarioService.alterarSenha(usuario.getLogin(), dados.senhaAtual(), dados.novaSenha());
        return ResponseEntity.noContent().build();
    }
}
