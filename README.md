# Sistema de Agendamento para Barbearia

Projeto academico desenvolvido em Java com foco em Programacao Orientada a Objetos (POO), integracao com banco de dados MySQL e um front-end web para gerenciamento de agendamentos de uma barbearia.

O sistema simula o funcionamento de uma barbearia, permitindo cadastro de clientes, barbeiros, serviços e controle de horarios agendados.

## Funcionalidades

- Cadastro de clientes
- Login de cliente e barbeiro
- Cadastro de barbeiros
- Cadastro de serviços
- Criação de agendamentos
- Listagem de agendamentos
- Painel do cliente
- Painel do barbeiro
- Alteração de horário pelo barbeiro
- Cancelamento de agendamentos
- Verificação de horário indisponivel para o barbeiro

## Tecnologias Utilizadas

### Front-end

- HTML
- CSS
- JavaScript

### Back-end

- Java
- API HTTP com `HttpServer`
- JDBC
- Programacao Orientada a Objetos

### Banco de Dados

- MySQL

## Como Rodar o Projeto

### 1. Configurar o Banco de Dados

Crie um banco de dados MySQL chamado:

```sql
CREATE DATABASE barbearia_db;
```

O projeto usa as seguintes configurções padrao:

```text
URL: jdbc:mysql://localhost:3306/barbearia_db
Usuario: root
Senha: definida no arquivo ConexaoDB.java
```

As configurações ficam em:

```text
src/util/ConexaoDB.java
```

Também e possível configurar a conexão usando variaveis de ambiente:

```text
BARBEARIA_DB_URL
BARBEARIA_DB_USUARIO
BARBEARIA_DB_SENHA
```

### 2. Compilar o Back-end

Dentro da pasta `Backend`, execute:

```powershell
javac -encoding UTF-8 -d out\production\SistemadeAgendamentoBarbearia src\*.java src\dao\*.java src\model\*.java src\service\*.java src\util\*.java
```

### 3. Rodar a API

Execute o comando abaixo, trocando `CAMINHO_DO_MYSQL_CONNECTOR.jar` pelo caminho do arquivo `.jar` do MySQL Connector:

```powershell
java -cp "out\production\SistemadeAgendamentoBarbearia;CAMINHO_DO_MYSQL_CONNECTOR.jar" ApiServer
```

Se tudo estiver correto, a API sera iniciada em:

```text
http://localhost:8080
```

### 4. Abrir o Front-end

Abra o arquivo abaixo no navegador:

```text
Frontend/Index.html
```

O front-end se comunica com a API pelo endereco:

```text
http://localhost:8080/api
```
## Telas do Sistema

- Pagina inicial
- Tela de cadastro
- Tela de login
- Tela de agendamento
- Painel do cliente
- Painel do barbeiro

## Objetivo do Projeto

O objetivo do projeto e aplicar conceitos de desenvolvimento back-end, Programação Orientada a Objetos, banco de dados e integração com front-end em uma solução voltada para automação de pequenos negocios.
