package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.ong.DadosAtualizacaoOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.ong.DadosCadastroOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosDetalhamentoOng;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.ong.DadosListagemOng;
import br.com.fiap3esph.trajetoria.application.port.in.StdFeaturePort;
import br.com.fiap3esph.trajetoria.application.service.OngService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/ongs")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "ONGs", description = "Gestão das ONGs parceiras da TRAJEtória")
@RequiredArgsConstructor
public class OngController implements StdFeaturePort<
        DadosCadastroOng,
        DadosListagemOng,
        DadosAtualizacaoOng,
        Void,
        DadosDetalhamentoOng,
        Long
        > {
    private final OngService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoOng> cadastrar(
            @RequestBody @Valid DadosCadastroOng dados,
            UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoOng dto = service.cadastrarOng(dados);
        URI uri = uriBuilder.path("/ongs/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG')")
    public ResponseEntity<Page<DadosListagemOng>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listarOngs(paginacao));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG')")
    public ResponseEntity<DadosDetalhamentoOng> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharOng(id));
    }

    @Override
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoOng> atualizar(
            @RequestBody @Valid DadosAtualizacaoOng dados) {
        return ResponseEntity.ok(service.atualizarOng(dados));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirOng(id);
        return ResponseEntity.noContent().build();
    }
}
