package br.com.fiap3esph.trajetoria.adapter.in.controller.request.jovem;

import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEndereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroJovem(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String telefone,

        @NotNull
        Tamanho tamanho,

        @NotNull
        @JsonProperty("id_ong")
        Long idOng,

        @NotNull
        @Valid
        DadosEndereco endereco,

        @NotNull
        @AssertTrue(message = "É necessário aceitar o termo de consentimento LGPD!")
        @JsonProperty("consentimento_aceito")
        Boolean consentimentoAceito) {
}
