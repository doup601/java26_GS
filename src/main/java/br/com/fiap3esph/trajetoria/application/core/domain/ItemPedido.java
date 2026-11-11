package br.com.fiap3esph.trajetoria.application.core.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemPedido {
    private Long id;
    private Peca peca;
    private Boolean serviu;

    public void registrarFeedback(Boolean serviu) {
        this.serviu = serviu;
    }
}
