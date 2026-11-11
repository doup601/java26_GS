package br.com.fiap3esph.trajetoria.shared.vo.endereco.dto;

public record DadosEnderecoCep(
        String logradouro,
        String bairro,
        String cidade,
        String uf,
        String cep) {
}
