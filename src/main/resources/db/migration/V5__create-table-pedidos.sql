CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    jovem_id BIGINT NOT NULL,
    data_solicitacao DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL,
    motivo_cancelamento VARCHAR(30),
    data_cancelamento DATETIME,
    entregador VARCHAR(255),
    custo_entrega DECIMAL(10, 2),
    data_despacho DATETIME,
    foi_entrevista TINYINT(1),
    comentario_feedback VARCHAR(500),
    data_feedback DATETIME,
    CONSTRAINT fk_pedidos_jovens FOREIGN KEY (jovem_id) REFERENCES jovens (id)
);
