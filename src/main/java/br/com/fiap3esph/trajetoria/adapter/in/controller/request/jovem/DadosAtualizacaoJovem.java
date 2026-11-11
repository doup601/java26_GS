package br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem;

import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEndereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoJovem(
        @NotNull
        @JsonProperty("id")
        Long id,
        String nome,
        @Email
        String email,
        String telefone,
        Tamanho tamanho,
        @Valid
        DadosEndereco endereco) {
}
