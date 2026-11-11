package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem.DadosAtualizacaoJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem.DadosCadastroJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosDetalhamentoJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosListagemJovem;
import br.com.fiap3esph.trajetoria.application.service.JovemService;
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
@RequestMapping("/jovens")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Jovens", description = "Cadastro e validação dos jovens atendidos pelas ONGs")
@RequiredArgsConstructor
public class JovemController {
    private final JovemService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG')")
    public ResponseEntity<DadosDetalhamentoJovem> cadastrar(
            @RequestBody @Valid DadosCadastroJovem dados,
            UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoJovem dto = service.cadastrarJovem(dados);
        URI uri = uriBuilder.path("/jovens/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG', 'OPERACAO')")
    public ResponseEntity<Page<DadosListagemJovem>> listar(
            @RequestParam(name = "ong", required = false) Long ongId,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return ResponseEntity.ok(service.listarJovens(ongId, paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG', 'OPERACAO')")
    public ResponseEntity<DadosDetalhamentoJovem> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharJovem(id));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG')")
    public ResponseEntity<DadosDetalhamentoJovem> atualizar(
            @RequestBody @Valid DadosAtualizacaoJovem dados) {
        return ResponseEntity.ok(service.atualizarJovem(dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirJovem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/validar")
    @PreAuthorize("hasAnyRole('ADMIN', 'ONG')")
    @Operation(summary = "Valida o jovem indicado pela ONG", description = "Permite que o jovem solicite pedidos de roupas.")
    public ResponseEntity<DadosDetalhamentoJovem> validar(@PathVariable Long id) {
        return ResponseEntity.ok(service.validarJovem(id));
    }
}
