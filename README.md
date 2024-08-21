## MeetSpace

## Visão Geral

Este sistema permite que os usuários reservem salas de reunião em um escritório. Ele inclui verificação de disponibilidade e um painel de administração para gerenciar reservas.

## Requisitos

- Java 17 ou superior
- PostgreSQL 16 ou superior
- Node.js 14 ou superior
- React 17 ou superior

## Instalação

1. Clone o repositório:
   ```
   git clone https://github.com/balchaki/sistema-reserva-salas.git
   ```

2. Configure o banco de dados PostgreSQL:
   - Crie um novo banco de dados
   - Execute o script SQL fornecido em `create_tables.sql`

3. Configure o backend:
   - Navegue até a pasta do backend
   - Copie `application.properties.example` para `application.properties` e configure as credenciais do banco de dados
   - Execute `./mvnw spring-boot:run` para iniciar o servidor

4. Configure o frontend:
   - Navegue até a pasta do frontend
   - Execute `npm install` para instalar as dependências
   - Execute `npm start` para iniciar o servidor de desenvolvimento

## Uso

1. Acesse o sistema através do navegador em `http://localhost:3000`
2. Faça login com suas credenciais
3. Na página principal, você verá a lista de salas disponíveis
4. Clique em "Reservar" para fazer uma reserva

## Funcionalidades Principais

1. **Reserva de Salas**: Os usuários podem visualizar salas disponíveis e fazer reservas.
2. **Verificação de Disponibilidade**: O sistema verifica automaticamente se uma sala está disponível antes de permitir a reserva.
3. **Notificações por E-mail**: Os usuários recebem notificações por e-mail sobre suas reservas.
4. **Painel de Administração**: Os administradores podem gerenciar reservas, salas e usuários.

## Suporte

Para obter suporte, entre em contato com a equipe de TI através do e-mail suporte@empresa.com.
