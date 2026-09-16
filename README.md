# CampusRide

API REST para organizar caronas entre estudantes de um campus. A aplicação permite publicar, consultar e cancelar caronas, além de reservar e cancelar vagas.

## Tecnologias

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database
- Gradle

## Como executar

Pré-requisito: ter o Java 25 configurado na máquina.

No Windows, abra um terminal na raiz do projeto e execute:

```powershell
.\gradlew.bat bootRun
```

A API ficará disponível em `http://localhost:8080`.

Para executar os testes automatizados:

```powershell
.\gradlew.bat test
```

> O banco H2 é mantido em memória. Ao encerrar a aplicação, os dados criados são apagados.

## Console H2

Por segurança, o console fica desabilitado na execução normal. Para habilitá-lo somente no ambiente de desenvolvimento, execute:

```powershell
.\gradlew.bat bootRun --args="--spring.profiles.active=dev"
```

Em seguida, acesse `http://localhost:8080/h2-console`.

Use os dados abaixo para conectar:

| Campo | Valor |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:campusride` |
| User Name | `sa` |
| Password | *(vazio)* |

## Endpoints

| Método | Rota | Descrição | Sucesso esperado |
| --- | --- | --- | --- |
| `POST` | `/caronas` | Publica uma carona | `201 Created` |
| `GET` | `/caronas` | Lista as caronas disponíveis | `200 OK` |
| `GET` | `/caronas/{id}` | Detalha uma carona e suas reservas | `200 OK` |
| `POST` | `/caronas/{caronaId}/reservas` | Cria uma reserva de vaga | `201 Created` |
| `PATCH` | `/reservas/{id}/cancelamento` | Cancela uma reserva | `200 OK` |
| `PATCH` | `/caronas/{id}/cancelamento` | Cancela uma carona e suas reservas | `200 OK` |

### Publicar uma carona

`POST /caronas`

```powershell
curl.exe -i -X POST "http://localhost:8080/caronas" `
  -H "Content-Type: application/json" `
  --data-raw '{"motorista":"Ana Souza","origem":"Campus Norte","destino":"Campus Sul","dataHoraPartida":"2030-12-20T08:00:00","tipoVeiculo":"CARRO","vagasTotais":2}'
```

Tipos de veículo aceitos: `MOTO`, `CARRO`, `SUV` e `VAN`.

### Listar caronas disponíveis

`GET /caronas`

```powershell
curl.exe -i "http://localhost:8080/caronas"
```

São retornadas somente caronas abertas cuja partida ainda está no futuro, ordenadas pelo horário de partida.

### Consultar o detalhe de uma carona

`GET /caronas/{id}`

```powershell
curl.exe -i "http://localhost:8080/caronas/1"
```

O detalhe inclui as reservas associadas à carona.

### Reservar uma vaga

`POST /caronas/{caronaId}/reservas`

```powershell
curl.exe -i -X POST "http://localhost:8080/caronas/1/reservas" `
  -H "Content-Type: application/json" `
  --data-raw '{"passageiro":"Bruno Lima"}'
```

### Cancelar uma reserva

`PATCH /reservas/{id}/cancelamento`

```powershell
curl.exe -i -X PATCH "http://localhost:8080/reservas/1/cancelamento"
```

### Cancelar uma carona

`PATCH /caronas/{id}/cancelamento`

```powershell
curl.exe -i -X PATCH "http://localhost:8080/caronas/1/cancelamento"
```

Os endpoints de cancelamento não recebem corpo na requisição.

## Regras de negócio

- A data e hora de partida devem estar no futuro.
- A quantidade de vagas deve ser maior ou igual a 1 e compatível com o veículo: moto até 1, carro até 5, SUV até 7 e van até 15.
- Uma carona só aceita reservas enquanto estiver `ABERTA` e possuir vagas.
- Não é possível reservar depois do horário de partida.
- A carona muda para `LOTADA` quando todas as vagas forem reservadas.
- Ao cancelar uma reserva de carona lotada, a carona volta para `ABERTA`.
- Uma reserva não pode ser cancelada duas vezes nem após a conclusão da carona.
- Ao cancelar uma carona, todas as suas reservas são canceladas em cascata.
- A API retorna erros padronizados, sem expor detalhes internos da aplicação.
- As reservas concorrentes são serializadas para impedir que a capacidade seja ultrapassada.

## Evidências de testes manuais

Os testes foram realizados no Postman, com a aplicação em execução localmente. As capturas estão na pasta [`prints`](prints):

| Cenário | Evidência |
| --- | --- |
| Criação de carona | [01-criar-carona-201-created.png](prints/01-criar-carona-201-created.png) |
| Listagem de caronas | [02-listar-caronas-200-ok.png](prints/02-listar-caronas-200-ok.png) |
| Detalhamento de carona | [03-detalhar-carona-200-ok.png](prints/03-detalhar-carona-200-ok.png) |
| Criação de reserva | [04-criar-reserva-201-created.png](prints/04-criar-reserva-201-created.png) |
| Carona com reserva | [05-detalhar-carona-com-reserva-200-ok.png](prints/05-detalhar-carona-com-reserva-200-ok.png) |
| Carona lotada e tentativa de reserva sem vaga | [06](prints/06-criar-segunda-reserva-201-created.png), [07](prints/07-carona-lotada-200-ok.png) e [08](prints/08-reserva-em-carona-lotada-400-bad-request.png) |
| Cancelamento de reserva e reabertura de vaga | [09](prints/09-cancelar-reserva-200-ok.png) e [10](prints/10-carona-reaberta-apos-cancelamento-200-ok.png) |
| Cancelamento em cascata da carona | [11-cancelar-carona-e-reservas-200-ok.png](prints/11-cancelar-carona-e-reservas-200-ok.png) |
| Validações de entrada | [12](prints/12-validacao-data-passada-400-bad-request.png) e [13](prints/13-validacao-capacidade-veiculo-400-bad-request.png) |

## Organização do código

```text
src/main/java/br/com/fiap/campusride
├── controller    # Endpoints HTTP
├── dto           # Dados de entrada e saída da API
├── exception     # Tratamento centralizado de erros
├── model         # Entidades e enums do domínio
├── repository    # Persistência com JPA
├── service       # Regras de negócio
└── validation    # Validação de capacidade por veículo
```
