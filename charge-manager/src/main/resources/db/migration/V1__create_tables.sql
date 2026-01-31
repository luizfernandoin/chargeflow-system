CREATE TABLE asaas_event (
    id SERIAL PRIMARY KEY,
    event VARCHAR(50),
    payload TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
