# EduSpace

Aplicação web para controle da ocupação de ambientes de ensino, desenvolvida com Java, ZK Framework e Spring Boot.

## Funcionalidades

- cadastro, edição e exclusão de alunos;
- cadastro, edição e exclusão de ambientes;
- registro de entrada e saída;
- ocupação atual e percentual por ambiente;
- controle de capacidade máxima;
- histórico preservado ao impedir a exclusão de registros relacionados.

## Tecnologias

- Java 17;
- ZK Framework 10;
- Spring Boot 3;
- Spring Data JPA;
- PostgreSQL em execução normal;
- Flyway para versionamento do esquema do banco de dados;
- H2 em memória nos testes automatizados.

## Como executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois, acesse `http://localhost:8080`.

Para execução local sem Docker, é necessário ter um PostgreSQL disponível na porta `5432`, com banco, usuário e senha `educzk`. Esses valores podem ser alterados pelas variáveis `DATABASE_URL`, `DATABASE_USERNAME` e `DATABASE_PASSWORD`.

## Execução com Docker

Com Docker e Docker Compose instalados, execute:

```powershell
docker compose up --build
```

A aplicação ficará disponível em `http://localhost:8081`. O Compose inicia um PostgreSQL 16 em `localhost:5434` e mantém os dados no volume `postgres_data`.

### Acesso ao PostgreSQL do Docker

Para conectar ao banco pelo Windows, DBeaver ou outro cliente externo ao Docker, use:

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `5434` |
| Banco | `educzk` |
| Usuário | `educzk` |
| Senha | `educzk` |

Entre os contêineres do Compose, a aplicação acessa o banco por `jdbc:postgresql://postgres:5432/educzk`. O nome `postgres` é o serviço do banco na rede interna do Docker.

Para executar a aplicação localmente e conectá-la ao PostgreSQL do Docker, configure a URL antes de iniciá-la:

```powershell
$env:DATABASE_URL = "jdbc:postgresql://localhost:5434/educzk"
$env:DATABASE_USERNAME = "educzk"
$env:DATABASE_PASSWORD = "educzk"
.\mvnw.cmd spring-boot:run
```

As principais tabelas usam nomes no plural: `alunos`, `ambientes`, `presencas` e `usuarios`. Exemplo de verificação:

```sql
SELECT
    (SELECT COUNT(*) FROM alunos) AS alunos,
    (SELECT COUNT(*) FROM ambientes) AS ambientes,
    (SELECT COUNT(*) FROM usuarios) AS usuarios;
```

As migrations ficam em `src/main/resources/db/migration`. A primeira versão do esquema é a migration `V1__criar_estrutura_inicial.sql`. Em bancos existentes, o Flyway cria o baseline sem apagar os dados; em bancos novos, executa a V1 automaticamente.

Para encerrar os contêineres sem apagar os dados:

```powershell
docker compose down
```

As credenciais iniciais da aplicação são `admin` / `admin`.

## Regras principais

- a matrícula do aluno é única;
- um aluno não pode ocupar dois ambientes ao mesmo tempo;
- não é permitida uma entrada quando o ambiente atinge sua capacidade;
- a capacidade não pode ser reduzida abaixo da ocupação atual;
- alunos e ambientes com histórico de presença não podem ser excluídos.
