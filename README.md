# TRAJEtória

## Ficha da CitizenTech

| Item | Descrição |
| --- | --- |
| **Nome da solução** | TRAJEtória |
| **Localização** | São Caetano do Sul / Região do ABC — SP |
| **Cidadão beneficiado** | Jovens em situação de vulnerabilidade social, de 16 a 24 anos, indicados por ONGs parceiras, que precisam de roupas sociais adequadas para entrevistas de emprego, processos seletivos e primeiros dias de trabalho. |
| **Problema** | Jovens em vulnerabilidade não têm roupas sociais adequadas para entrevistas e oportunidades de trabalho, enquanto doações de roupas chegam às ONGs de forma desorganizada, sem rastreio, sem curadoria de tamanho e sem garantia de que cheguem a quem realmente precisa. |
| **Como a solução resolve** | A TRAJEtória é uma API que conecta ONGs, operação logística e jovens em um único fluxo: as ONGs cadastram e validam os jovens; a operação organiza o acervo de peças (com categoria, tamanho, estado de conservação e foto); o jovem solicita um pedido de até 5 peças; a operação aprova, separa, despacha e entrega; e o jovem devolve feedback informando quais peças realmente serviram, realimentando o acervo. |
| **Partes envolvidas** | ONGs parceiras (cadastro e validação dos jovens), operação da TRAJEtória (gestão do acervo e logística de entrega), jovens beneficiados (solicitação e feedback) e administração da plataforma. |
| **Repositório** | `https://github.com/<usuario>/trajetoria` *(substituir pelo link do repositório publicado)* |
| **Indicador de impacto** | Percentual de peças entregues que "serviram" segundo o feedback dos jovens (peças reprovadas voltam ao acervo) e número de jovens atendidos por mês. |
| **ODS relacionados** | **ODS 10** — Redução das Desigualdades (acesso a vestuário adequado para ingresso no mercado de trabalho); **ODS 8** — Trabalho Decente e Crescimento Econômico (apoio à empregabilidade de jovens); **ODS 12** — Consumo e Produção Responsáveis (reuso de roupas por meio da doação organizada). |

---

API REST desenvolvida com Spring Boot para a plataforma **TRAJEtória**: doação de roupas sociais para jovens em vulnerabilidade, indicados por ONGs parceiras. Projeto da **Global Solution CitizenTech — FIAP 2026** (disciplina SOA e Web Services).

## Integrantes

| Nome | RM |
| --- | --- |
| Pedro Gaspar Fernandes Ferrari | 554887 |
| Enrico Ricarte Rodrigues | 558571 |
| Victor Freire | 556191 |

## Tecnologias

- Java 25
- Spring Boot 4.0.5
- Spring Web MVC, Data JPA, Security, Validation
- Flyway (migrations)
- MySQL
- JWT (com.auth0 java-jwt)
- Springdoc OpenAPI (Swagger)
- Lombok

## Arquitetura

O projeto segue a arquitetura hexagonal (ports & adapters):

- `application/core/domain` — entidades de domínio puras (regras de negócio)
- `application/core/specification` — validadores de pedido (padrão specification)
- `application/port/in` e `application/port/out` — portas de entrada e saída
- `application/service` — regras de negócio / casos de uso
- `adapter/in` — controllers e DTOs de entrada/saída
- `adapter/out` — persistência (JPA) e integração com APIs externas
- `config` — segurança, documentação e configuração de integrações

## Modelo de domínio

- **Ong** — parceira que indica e valida os jovens.
- **Jovem** — beneficiado, vinculado a uma ONG, com tamanho de roupa e endereço de entrega; precisa ser validado pela ONG antes de pedir.
- **Peca** — item do acervo com categoria, tamanho, estado de conservação, foto e status (`DISPONIVEL → RESERVADA → SEPARADA → DESPACHADA → ENTREGUE`, além de `DESCARTADA`).
- **Pedido** — solicitação de um jovem, com até 5 peças, e o fluxo de status `SOLICITADO → APROVADO → DESPACHADO → ENTREGUE` (ou `CANCELADO`).
- **ItemPedido** — peça dentro de um pedido, com o feedback se a peça serviu ou não.
- **Usuario** — acesso à API com perfis `ADMIN`, `OPERACAO`, `ONG` e `JOVEM`.

## Integração com API externa

O endereço de entrega dos jovens é enriquecido automaticamente a partir do CEP informado, consultando o WebService público **ViaCEP** (`https://viacep.com.br/ws/{cep}/json/`). Quando o CEP não é encontrado, os campos de endereço podem ser informados manualmente. Também há um endpoint dedicado de consulta de CEP: `GET /cep/{cep}`.

## Como executar

Pré-requisitos:

- Java 17 ou superior (projeto configurado para Java 25)
- MySQL rodando em `localhost:3306`

Credenciais de conexão (padrão no `application.properties`):

```
spring.datasource.url=jdbc:mysql://localhost:3306/trajetoria?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=fiap
```

Subir a aplicação (usa o Maven Wrapper):

```
./mvnw spring-boot:run
```

Ou no Windows:

```
mvnw.cmd spring-boot:run
```

A aplicação sobe na porta `8085`. O Flyway cria automaticamente o banco, as tabelas e o usuário administrador inicial.

## Documentação (Swagger)

Com a aplicação em execução, a documentação interativa está disponível em:

- Swagger UI: `http://localhost:8085/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8085/v3/api-docs`

O arquivo `SwaggerOffLine.yaml` na raiz contém a especificação OpenAPI exportada.

## Como testar

Usuário administrador inicial:

```
login: admin
senha: admin
```

1. `POST /login` com `{"login": "admin", "senha": "admin"}` retorna um `tokenJWT`.
2. Use o header `Authorization: Bearer <tokenJWT>` em todas as demais requisições.
3. Como `ADMIN`, cadastre uma ONG (`POST /ongs`) e usuários com os demais perfis (`POST /usuarios`).
4. Cadastre um jovem vinculado à ONG (`POST /jovens`), valide-o (`POST /jovens/{id}/validar`) e cadastre peças no acervo (`POST /pecas`).
5. Com um token de perfil `JOVEM`, crie o pedido (`POST /pedidos`) e siga o fluxo: `aprovar` → `despachar` → `entregar` → `feedback`.

### Endpoints

| Método | Rota | Permissão |
| --- | --- | --- |
| POST | `/login` | público |
| PUT | `/login/senha` | autenticado |
| GET/POST/PUT/DELETE | `/usuarios` e `/usuarios/{id}` | ADMIN |
| GET/POST/PUT/DELETE | `/ongs` e `/ongs/{id}` | GET: ADMIN/OPERACAO/ONG, demais: ADMIN |
| GET/POST/PUT/DELETE | `/jovens` e `/jovens/{id}` | POST/PUT: ADMIN/ONG, GET: ADMIN/ONG/OPERACAO, DELETE: ADMIN |
| POST | `/jovens/{id}/validar` | ADMIN / ONG |
| GET/POST/PUT/DELETE | `/pecas` e `/pecas/{id}` | GET: autenticado, demais: ADMIN/OPERACAO |
| POST | `/pecas/{id}/descartar` | ADMIN / OPERACAO |
| POST | `/pedidos` | JOVEM |
| GET | `/pedidos` e `/pedidos/{id}` | ADMIN / OPERACAO / ONG (JOVEM também pode detalhar) |
| POST | `/pedidos/{id}/aprovar` | ADMIN / OPERACAO / ONG |
| POST | `/pedidos/{id}/despachar` | ADMIN / OPERACAO |
| POST | `/pedidos/{id}/entregar` | ADMIN / OPERACAO |
| POST | `/pedidos/{id}/cancelar` | ADMIN / OPERACAO / ONG / JOVEM |
| POST | `/pedidos/{id}/feedback` | ADMIN / OPERACAO / ONG / JOVEM |
| GET | `/cep/{cep}` | autenticado |
| GET | `/health-check` | autenticado |

O acervo pode ser filtrado em `GET /pecas?status=DISPONIVEL&categoria=CAMISA&tamanho=M`. Os pedidos podem ser filtrados pela ONG do jovem em `GET /pedidos?ong=1`.

### Regras de negócio

- Um jovem só pode criar pedidos depois de validado por uma ONG.
- O jovem precisa aceitar o termo de consentimento (LGPD) no cadastro, com registro de data.
- Um pedido pode conter no máximo 5 peças.
- Todas as peças do pedido precisam estar `DISPONIVEL` e no tamanho do jovem (validação de tamanho).
- O custo de entrega não pode exceder R$ 50,00.
- O pedido só pode ser cancelado antes do despacho; o cancelamento devolve as peças ao acervo.
- O feedback só pode ser registrado após a entrega; peças marcadas como "não serviu" voltam ao acervo como `DISPONIVEL`.
- Peças em pedidos ativos não podem ser descartadas; o descarte exige motivo.

## Testes automatizados

Os testes de cada camada podem ser executados com:

```
./mvnw test
```

Ou no Windows:

```
mvnw.cmd test
```

Cobrem: fluxo de domínio do pedido (transições de status), validadores de pedido, persistência de ONG/Jovem/Peça/Usuário e um teste de controller (requisição inválida → 400). Os testes de persistência utilizam o perfil `test` e o banco `trajetoria_test` (o Flyway cria o schema automaticamente).

## Estrutura do projeto

```
src/main/java/br/com/fiap3esph/trajetoria
├── adapter
│   ├── in/controller          # controllers, DTOs e mappers
│   └── out
│       ├── integration        # ViaCEP
│       ├── repository         # JPA + entity mappers
│       └── security           # autenticação
├── application
│   ├── core/domain            # Ong, Jovem, Peca, Pedido, ItemPedido, Usuario
│   ├── core/specification     # validadores de pedido
│   ├── port/in
│   ├── port/out
│   └── service
├── config
│   ├── documentation          # Swagger/OpenAPI
│   ├── integration            # RestClient
│   └── security               # JWT / CORS
├── exception
└── shared
```

## Escopo e limitações

- O `Usuario` (autenticação) não é vinculado 1:1 ao `Jovem`: a autorização é feita por perfil (role), não por propriedade do recurso.
- Doador, Lote, Patrocinador e Pacote ficaram fora do escopo desta entrega; a origem da peça é registrada como texto livre.
- O despacho ocorre em dia fixo semanal (simplificado): a operação chama o endpoint de despacho quando o pedido está aprovado.
