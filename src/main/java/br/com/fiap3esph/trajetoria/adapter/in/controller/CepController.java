package br.com.fiap3esph.trajetoria.adapter.in.controller;

import br.com.fiap3esph.trajetoria.application.service.EnderecoService;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEnderecoCep;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CepController {
    private final EnderecoService enderecoService;

    @GetMapping("/{cep}")
    public ResponseEntity<DadosEnderecoCep> consultarCep(@PathVariable String cep) {
        return enderecoService
                .consultarCep(cep)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
