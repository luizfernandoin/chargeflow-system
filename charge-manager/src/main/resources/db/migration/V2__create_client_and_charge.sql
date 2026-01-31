CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    cpf_cnpj VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE charge (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES client(id),
    value NUMERIC(10,2),
    type VARCHAR(20),
    status VARCHAR(20),
    asaas_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);