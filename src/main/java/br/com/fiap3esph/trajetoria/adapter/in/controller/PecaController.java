package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosAtualizacaoPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosCadastroPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.peca.DadosDescarte;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosDetalhamentoPeca;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.peca.DadosListagemPeca;
import br.com.fiap3esph.trajetoria.application.service.PecaService;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/pecas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Peças", description = "Acervo de peças de roupa disponíveis para doação")
@RequiredArgsConstructor
public class PecaController {
    private final PecaService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    public ResponseEntity<DadosDetalhamentoPeca> cadastrar(
            @RequestBody @Valid DadosCadastroPeca dados,
            UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoPeca dto = service.cadastrarPeca(dados);
        URI uri = uriBuilder.path("/pecas/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lista o acervo com filtros opcionais de status, categoria e tamanho")
    public ResponseEntity<Page<DadosListagemPeca>> listar(
            @RequestParam(required = false) StatusPeca status,
            @RequestParam(required = false) CategoriaPeca categoria,
            @RequestParam(required = false) Tamanho tamanho,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listarPecas(status, categoria, tamanho, paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DadosDetalhamentoPeca> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharPeca(id));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    public ResponseEntity<DadosDetalhamentoPeca> atualizar(
            @RequestBody @Valid DadosAtualizacaoPeca dados) {
        return ResponseEntity.ok(service.atualizarPeca(dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirPeca(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/descartar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    @Operation(summary = "Descarta uma peça do acervo informando o motivo")
    public ResponseEntity<DadosDetalhamentoPeca> descartar(
            @PathVariable Long id,
            @RequestBody @Valid DadosDescarte dados) {
        return ResponseEntity.ok(service.descartarPeca(id, dados));
    }
}
