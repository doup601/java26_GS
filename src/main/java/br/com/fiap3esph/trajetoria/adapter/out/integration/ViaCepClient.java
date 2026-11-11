package br.com.fiap3esph.trajetoria.adapter.out.integration;

import br.com.fiap3esph.trajetoria.adapter.out.integration.dto.ViaCepResponse;
import br.com.fiap3esph.trajetoria.application.port.out.ConsultaCepPort;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEnderecoCep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViaCepClient implements ConsultaCepPort {
    private final RestClient viaCepRestClient;

    @Override
    public Optional<DadosEnderecoCep> consultar(String cep) {
        String cepLimpo = limparCep(cep);
        if (cepLimpo == null) {
            return Optional.empty();
        }

        try {
            ViaCepResponse response = viaCepRestClient
                    .get()
                    .uri("/{cep}/json/", cepLimpo)
                    .retrieve()
                    .body(ViaCepResponse.class);

            if (response == null || response.erro() != null) {
                return Optional.empty();
            }

            return Optional.of(new DadosEnderecoCep(
                    response.logradouro(),
                    response.bairro(),
                    response.cidade(),
                    response.uf(),
                    response.cep()
            ));
        } catch (Exception e) {
            log.warn("Não foi possível consultar o ViaCEP para o cep {}: {}", cep, e.getMessage());
            return Optional.empty();
        }
    }

    private String limparCep(String cep) {
        if (cep == null) {
            return null;
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        return cepLimpo.length() == 8 ? cepLimpo : null;
    }
}
