package br.com.fiap3esph.trajetoria.application.core.specification.contract;

import br.com.fiap3esph.trajetoria.adapter.in.controller.request.pedido.DadosPedido;

public interface ValidadorPedido {
    void validar(DadosPedido dados);
}
