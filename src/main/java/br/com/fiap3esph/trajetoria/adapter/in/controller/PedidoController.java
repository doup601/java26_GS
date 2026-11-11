package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosCancelamento;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosDespacho;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosFeedback;
import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido.DadosListagemPedido;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.pedido.DetalhamentoPedido;
import br.com.fiap3esph.trajetoria.application.service.PedidoService;
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
@RequestMapping("/pedidos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Pedidos", description = "Fluxo completo do pedido: solicitação, aprovação, despacho, entrega e feedback")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService service;

    @PostMapping
    @PreAuthorize("hasRole('JOVEM')")
    @Operation(summary = "Cria um pedido de roupas para um jovem validado")
    public ResponseEntity<DetalhamentoPedido> cadastrar(
            @RequestBody @Valid DadosPedido dados,
            UriComponentsBuilder uriBuilder) {
        DetalhamentoPedido dto = service.criarPedido(dados);
        URI uri = uriBuilder.path("/pedidos/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG')")
    @Operation(summary = "Lista os pedidos, opcionalmente filtrando pela ONG do jovem")
    public ResponseEntity<Page<DadosListagemPedido>> listar(
            @RequestParam(name = "ong", required = false) Long ongId,
            @ParameterObject @PageableDefault(size = 10, sort = "dataSolicitacao") Pageable paginacao) {
        return ResponseEntity.ok(service.listarPedidos(ongId, paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG', 'JOVEM')")
    public ResponseEntity<DetalhamentoPedido> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharPedido(id));
    }

    @PostMapping("/{id}/aprovar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG')")
    @Operation(summary = "Aprova o pedido e separa as peças no acervo")
    public ResponseEntity<DetalhamentoPedido> aprovar(@PathVariable Long id) {
        return ResponseEntity.ok(service.aprovarPedido(id));
    }

    @PostMapping("/{id}/despachar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    @Operation(summary = "Despacha o pedido informando entregador e custo de entrega (máximo R$ 50,00)")
    public ResponseEntity<DetalhamentoPedido> despachar(
            @PathVariable Long id,
            @RequestBody @Valid DadosDespacho dados) {
        return ResponseEntity.ok(service.despacharPedido(id, dados));
    }

    @PostMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO')")
    @Operation(summary = "Confirma a entrega do pedido ao jovem")
    public ResponseEntity<DetalhamentoPedido> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmarEntrega(id));
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG', 'JOVEM')")
    @Operation(summary = "Cancela o pedido antes do despacho devolvendo as peças ao acervo")
    public ResponseEntity<DetalhamentoPedido> cancelar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCancelamento dados) {
        return ResponseEntity.ok(service.cancelarPedido(id, dados));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERACAO', 'ONG', 'JOVEM')")
    @Operation(summary = "Registra o feedback do jovem após a entrega")
    public ResponseEntity<DetalhamentoPedido> feedback(
            @PathVariable Long id,
            @RequestBody @Valid DadosFeedback dados) {
        return ResponseEntity.ok(service.registrarFeedback(id, dados));
    }
}
