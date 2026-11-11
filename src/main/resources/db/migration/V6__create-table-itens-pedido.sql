CREATE TABLE itens_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    peca_id BIGINT NOT NULL,
    serviu TINYINT(1),
    CONSTRAINT fk_itens_pedido_pedidos FOREIGN KEY (pedido_id) REFERENCES pedidos (id),
    CONSTRAINT fk_itens_pedido_pecas FOREIGN KEY (peca_id) REFERENCES pecas (id)
);
