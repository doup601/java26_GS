package br.com.fiap3esph.trajetoria.application.port.out;

import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEnderecoCep;

import java.util.Optional;

public interface ConsultaCepPort {
    Optional<DadosEnderecoCep> consultar(String cep);
}
