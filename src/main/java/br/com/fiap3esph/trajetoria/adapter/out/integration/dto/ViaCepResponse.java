package br.com.fiap3esph.trajetoria.adapter.out.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        @JsonProperty("localidade") String cidade,
        String uf,
        String erro) {
}
