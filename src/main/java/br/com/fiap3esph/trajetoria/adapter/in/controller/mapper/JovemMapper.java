package br.com.fiap3esph.trajetoria.adapter.in.controller.mapper;

import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosDetalhamentoJovem;
import br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem.DadosListagemJovem;
import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.mapper.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JovemMapper {
    private final EnderecoMapper enderecoMapper;

    public DadosDetalhamentoJovem toDetails(Jovem jovem) {
        return new DadosDetalhamentoJovem(
                jovem,
                enderecoMapper.toDto(jovem.getEndereco())
        );
    }

    public DadosListagemJovem toList(Jovem jovem) {
        return new DadosListagemJovem(
                jovem.getId(),
                jovem.getNome(),
                jovem.getEmail(),
                jovem.getTamanho()
        );
    }
}
