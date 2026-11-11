package br.com.fiap3esph.trajetoria.application.core.domain;

import br.com.fiap3esph.trajetoria.exception.type.ValidacaoException;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.CategoriaPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.EstadoConservacao;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.StatusPeca;
import br.com.fiap3esph.trajetoria.shared.vo.enumeration.Tamanho;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Peca {
    private Long id;
    private String nome;
    private CategoriaPeca categoria;
    private Tamanho tamanho;
    private EstadoConservacao estadoConservacao;
    private String fotoUrl;
    private String localArmazem;
    private String origem;
    private StatusPeca status;
    private String motivoDescarte;

    public void atualizarInformacoes(
            String nome,
            CategoriaPeca categoria,
            Tamanho tamanho,
            EstadoConservacao estadoConservacao,
            String fotoUrl,
            String localArmazem,
            String origem) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        if (categoria != null) {
            this.categoria = categoria;
        }
        if (tamanho != null) {
            this.tamanho = tamanho;
        }
        if (estadoConservacao != null) {
            this.estadoConservacao = estadoConservacao;
        }
        if (fotoUrl != null && !fotoUrl.isBlank()) {
            this.fotoUrl = fotoUrl;
        }
        if (localArmazem != null && !localArmazem.isBlank()) {
            this.localArmazem = localArmazem;
        }
        if (origem != null && !origem.isBlank()) {
            this.origem = origem;
        }
    }

    public void reservar() {
        exigirStatus(StatusPeca.DISPONIVEL, "Peça " + id + " não está disponível para reserva!");
        this.status = StatusPeca.RESERVADA;
    }

    public void separar() {
        exigirStatus(StatusPeca.RESERVADA, "Peça " + id + " não está reservada para separação!");
        this.status = StatusPeca.SEPARADA;
    }

    public void despachar() {
        exigirStatus(StatusPeca.SEPARADA, "Peça " + id + " não está separada para despacho!");
        this.status = StatusPeca.DESPACHADA;
    }

    public void confirmarEntrega() {
        exigirStatus(StatusPeca.DESPACHADA, "Peça " + id + " não está em trânsito para entrega!");
        this.status = StatusPeca.ENTREGUE;
    }

    public void devolverAoEstoque() {
        if (this.status == StatusPeca.DISPONIVEL) {
            throw new ValidacaoException("Peça " + id + " já está disponível!");
        }
        if (this.status == StatusPeca.DESPACHADA) {
            throw new ValidacaoException("Peça " + id + " está em trânsito e não pode retornar ao estoque!");
        }
        if (this.status == StatusPeca.DESCARTADA) {
            throw new ValidacaoException("Peça " + id + " foi descartada e não pode retornar ao estoque!");
        }
        this.status = StatusPeca.DISPONIVEL;
    }

    public void descartar(String motivo) {
        if (this.status == StatusPeca.DESCARTADA) {
            throw new ValidacaoException("Peça " + id + " já foi descartada!");
        }
        if (this.status != StatusPeca.DISPONIVEL) {
            throw new ValidacaoException("Peça " + id + " está em um pedido e não pode ser descartada!");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new ValidacaoException("Motivo do descarte é obrigatório!");
        }
        this.motivoDescarte = motivo;
        this.status = StatusPeca.DESCARTADA;
    }

    private void exigirStatus(StatusPeca esperado, String mensagem) {
        if (this.status != esperado) {
            throw new ValidacaoException(mensagem);
        }
    }
}
