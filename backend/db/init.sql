-- =============================================================
-- Portfolio SaaS — Script de inicialização do banco PostgreSQL
-- Executado automaticamente pelo Docker na primeira inicialização
-- =============================================================

-- Garante que estamos no banco correto
\connect portfolio_saas;

-- Extensão para UUID (opcional, mas útil)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================================
-- Tabela: users
-- =============================================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER'
                   CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')),
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Índices para buscas frequentes
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email    ON users (email);

-- =============================================================
-- Usuário admin padrão
-- Senha: password  (BCrypt com strength=10)
-- TROQUE A SENHA ANTES DE IR PARA PRODUÇÃO
-- =============================================================
INSERT INTO users (username, email, password, role, enabled)
VALUES (
    'admin',
    'admin@portfoliosass.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',  -- password: password
    'ROLE_ADMIN',
    TRUE
)
ON CONFLICT (username) DO NOTHING;
