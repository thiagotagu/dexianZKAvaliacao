INSERT INTO usuarios (login, senha, ativo)
SELECT
    'admin',
    '$2a$10$cP7J5J46Wvxrc3aKFCGJ8.8qUQfI.oIzpyGXqkpYmaAxPD01PJPGa',
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE lower(login) = 'admin'
);

INSERT INTO alunos (nome, matricula, email)
SELECT dados.nome, dados.matricula, dados.email
FROM (
    VALUES
        ('Ana Souza', '2026001', 'ana.souza@escola.edu.br'),
        ('Bruno Martins', '2026002', 'bruno.martins@escola.edu.br'),
        ('Camila Oliveira', '2026003', 'camila.oliveira@escola.edu.br'),
        ('Daniel Costa', '2026004', 'daniel.costa@escola.edu.br'),
        ('Eduarda Lima', '2026005', 'eduarda.lima@escola.edu.br'),
        ('Felipe Rocha', '2026006', 'felipe.rocha@escola.edu.br'),
        ('Gabriela Alves', '2026007', 'gabriela.alves@escola.edu.br'),
        ('Henrique Melo', '2026008', 'henrique.melo@escola.edu.br'),
        ('Isabela Ribeiro', '2026009', 'isabela.ribeiro@escola.edu.br'),
        ('João Ferreira', '2026010', 'joao.ferreira@escola.edu.br')
) AS dados(nome, matricula, email)
WHERE NOT EXISTS (
    SELECT 1 FROM alunos WHERE alunos.matricula = dados.matricula
);

INSERT INTO ambientes (nome, tipo, capacidade)
SELECT dados.nome, dados.tipo, dados.capacidade
FROM (
    VALUES
        ('Sala 101', 'SALA_DE_AULA', 35),
        ('Laboratório de Informática', 'LABORATORIO', 24),
        ('Sala de Estudos', 'SALA_DE_ESTUDOS', 18)
) AS dados(nome, tipo, capacidade)
WHERE NOT EXISTS (
    SELECT 1 FROM ambientes WHERE lower(ambientes.nome) = lower(dados.nome)
);
