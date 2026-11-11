package br.com.fiap3esph.trajetoria.adapter.in.controller.response.jovem;

import br.com.fiap3esph.trajetoria.application.core.domain.Jovem;
import br.com.fiap3esph.trajetoria.shared.vo.endereco.dto.DadosEndereco;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosDetalhamentoJovem(
        Long id,
        String nome,
        String email,
        String telefone,
        Tamanho tamanho,
        @JsonProperty("id_ong")
        Long idOng,
        @JsonProperty("nome_ong")
        String nomeOng,
        DadosEndereco endereco,
        boolean validado,
        @JsonProperty("consentimento_aceito")
        boolean consentimentoAceito,
        boolean ativo) {
    public DadosDetalhamentoJovem(Jovem jovem, DadosEndereco endereco) {
        this(
                jovem.getId(),
                jovem.getNome(),
                jovem.getEmail(),
                jovem.getTelefone(),
                jovem.getTamanho(),
                jovem.getOng() == null ? null : jovem.getOng().getId(),
                jovem.getOng() == null ? null : jovem.getOng().getNome(),
                endereco,
                jovem.isValidado(),
                jovem.isConsentimentoAceito(),
                jovem.isAtivo()
        );
    }
}
