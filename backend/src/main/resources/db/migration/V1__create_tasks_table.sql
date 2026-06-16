CREATE TABLE tasks (
    id           BIGSERIAL    PRIMARY KEY,
    titulo       VARCHAR(150) NOT NULL,
    descricao    VARCHAR(1000),
    status       VARCHAR(20)  NOT NULL,
    data_criacao TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_tasks_status ON tasks (status);
