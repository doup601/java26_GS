package br.com.fiap3esph.trajetoria.application.service;

import br.com.fiap3esph.trajetoria.application.port.out.ConsultaCepPort;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.Endereco;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEnderecoCep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final ConsultaCepPort consultaCepPort;

    public Endereco enriquecer(Endereco endereco) {
        if (endereco == null || endereco.getCep() == null || endereco.getCep().isBlank()) {
            return endereco;
        }
        return consultaCepPort.consultar(endereco.getCep())
                .map(cep -> new Endereco(
                        cep.logradouro(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        cep.bairro(),
                        cep.cidade(),
                        cep.uf(),
                        endereco.getCep()
                ))
                .orElse(endereco);
    }

    public Optional<DadosEnderecoCep> consultarCep(String cep) {
        return consultaCepPort.consultar(cep);
    }
}
