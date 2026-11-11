CREATE TABLE pecas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(30) NOT NULL,
    tamanho VARCHAR(20) NOT NULL,
    estado_conservacao VARCHAR(30) NOT NULL,
    foto_url VARCHAR(500) NOT NULL,
    local_armazem VARCHAR(255),
    origem VARCHAR(255),
    status VARCHAR(30) NOT NULL,
    motivo_descarte VARCHAR(255)
);
