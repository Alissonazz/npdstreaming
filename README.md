# NpdStreaming — API de Catálogo de Séries

API REST em **Java (Spring Boot)** para catálogo de séries de TV, permitindo consultar séries, temporadas, episódios, avaliações do IMDb, lançamentos recentes e filtrar por gênero, com dados persistidos em **PostgreSQL**.

## Sobre o Projeto

Este projeto expõe uma API REST que serve o catálogo de um serviço de streaming de séries. Os dados das séries e dos episódios (título, temporadas, nota IMDb, gênero, atores, pôster e sinopse) são obtidos originalmente da API do **OMDb**, convertidos para objetos do domínio e armazenados em um banco PostgreSQL. A carga inicial do catálogo é feita automaticamente pelo script `data.sql` na inicialização da aplicação.

A aplicação é construída em camadas (Controller → Service → Repository) com Spring Data JPA, retorna os dados por meio de DTOs e possui CORS configurado para ser consumida por um front-end local. Também acompanha `Dockerfile` e `docker-compose.yml` para subir a API e o banco com um único comando.

## Funcionalidades

- Listagem de todas as séries do catálogo
- Top 5 séries mais bem avaliadas (nota IMDb)
- Últimos lançamentos (séries com os episódios mais recentes)
- Busca de série por ID
- Listagem de episódios por série (todas as temporadas) ou por temporada
- Top 5 melhores episódios de uma série
- Filtro de séries por categoria (gênero)
- Integração com API externa (OMDb) para obtenção dos dados
- Conversão do JSON da API em objetos do domínio (`SerieData` / `EpisodeData` → `Series` / `Episodes`)
- Persistência com JPA/Hibernate e carga inicial via `data.sql`
- Execução containerizada com Docker Compose

## Estrutura do Projeto

```
npdstreaming/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── src/main/
    ├── java/com/npd/npdstreaming/
    │   ├── config/
    │   │   └── CorsConfiguration.java    # Configuração de CORS para o front-end
    │   ├── controller/
    │   │   └── SerieController.java      # Endpoints REST (/series)
    │   ├── dto/
    │   │   ├── SerieDto.java             # Dados de série retornados pela API
    │   │   └── EpisodeDto.java           # Dados de episódio retornados pela API
    │   ├── model/
    │   │   ├── Series.java               # Entidade JPA da série
    │   │   ├── Episodes.java             # Entidade JPA do episódio
    │   │   ├── Category.java             # Enum de gêneros (OMDb / pt-BR)
    │   │   ├── SerieData.java            # Record com o JSON de série do OMDb
    │   │   └── EpisodeData.java          # Record com o JSON de episódio do OMDb
    │   ├── repository/
    │   │   └── SeriesRepository.java     # Consultas JPA e JPQL
    │   ├── service/
    │   │   ├── SerieService.java         # Regras de negócio e conversão para DTO
    │   │   ├── ApiConsumption.java       # Consumo da API externa (OMDb)
    │   │   ├── DataConvert.java          # Conversão de JSON para objetos
    │   │   └── IDataConvert.java         # Interface do contrato de conversão
    │   └── NpdStreamingApplication.java  # Classe principal (Spring Boot)
    └── resources/
        ├── application.properties        # Configurações (banco, JPA, OMDb)
        └── data.sql                      # Carga inicial do catálogo
```

### Classes

| Classe | Responsabilidade |
|---|---|
| `NpdStreamingApplication` | Ponto de entrada da aplicação Spring Boot |
| `SerieController` | Expõe os endpoints REST sob `/series` |
| `SerieService` | Regras de negócio; converte entidades em `SerieDto` e `EpisodeDto` |
| `SeriesRepository` | Acesso ao banco: busca por gênero, Top 5, lançamentos recentes e episódios |
| `ApiConsumption` | Realiza as requisições HTTP à API do OMDb |
| `DataConvert` | Converte o JSON da API nos objetos `SerieData` e `EpisodeData` |
| `IDataConvert` | Interface que define o contrato de conversão de dados |
| `Series` | Entidade JPA com as informações da série e seus episódios |
| `Episodes` | Entidade JPA com as informações do episódio |
| `Category` | Enum de gêneros, com mapeamento entre nomes do OMDb e português |
| `SerieData` / `EpisodeData` | Records que mapeiam o JSON retornado pelo OMDb |
| `SerieDto` / `EpisodeDto` | Objetos de transferência retornados pelos endpoints |
| `CorsConfiguration` | Libera as origens do front-end local para chamar a API |

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/series` | Lista todas as séries |
| GET | `/series/top5` | Top 5 séries por nota IMDb |
| GET | `/series/releases` | 5 séries com lançamentos mais recentes |
| GET | `/series/{id}` | Busca uma série pelo ID |
| GET | `/series/{id}/seasons/all` | Todos os episódios da série |
| GET | `/series/{id}/seasons/{season}` | Episódios de uma temporada |
| GET | `/series/{id}/seasons/bests` | Top 5 episódios mais bem avaliados |
| GET | `/series/category/{categoryName}` | Séries de um gênero (ex.: `Action`, `Crime`, `Drama`) |

**Gêneros disponíveis:** `Action`, `Comedy`, `Drama`, `Crime`, `Adventure`, `Animation` e `Biography`.

## Como Executar

### Pré-requisitos

- [JDK 25](https://www.oracle.com/java/technologies/downloads/) instalado
- [Maven](https://maven.apache.org/download.cgi) instalado (ou uso do Maven Wrapper incluso no projeto)
- [PostgreSQL 17](https://www.postgresql.org/download/) ou [Docker](https://www.docker.com/) com Docker Compose

### Opção 1 — Docker Compose (recomendada)

```bash
# Clone o repositório
git clone https://github.com/Alissonazz/npdstreaming.git
cd npdstreaming

# Suba o banco e a API
docker compose up --build
```

A API ficará disponível em `http://localhost:8080`.

### Opção 2 — Execução local

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/npdstreaming.git
cd npdstreaming

# Configure as variáveis de ambiente
export DB_URL = jdbc:postgresql://localhost:5432/npdstreaming
export DB_PASSWORD = sua_senha

# Compile o projeto
./mvnw clean install

# Execute a aplicação
./mvnw spring-boot:run
```

### Variáveis de ambiente

| Variável | Descrição |
|---|---|
| `DB_URL` | URL JDBC do PostgreSQL (ex.: `jdbc:postgresql://localhost:5432/npdstreaming`) |
| `DB_PASSWORD` | Senha do usuário `postgres` |


> **Atenção:** o `data.sql` é executado a cada inicialização (`spring.sql.init.mode=always`) e começa apagando as tabelas `episodes` e `series` antes de recarregar o catálogo. Alterações feitas diretamente no banco serão perdidas ao reiniciar a aplicação.

## Exemplo de Uso

```bash
curl http://localhost:8080/series/5
```

```json
{
  "id": 5,
  "title": "Game of Thrones",
  "season": 8,
  "omdbRating": 9.2,
  "genre": "ACTION",
  "actors": "Emilia Clarke, Peter Dinklage, Kit Harington",
  "poster": "https://m.media-amazon.com/images/M/...",
  "sinopse": "Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia."
}
```

```bash
# Top 5 séries mais bem avaliadas
curl http://localhost:8080/series/top5

# Episódios da temporada 1 da série 5
curl http://localhost:8080/series/5/seasons/1

# Séries de crime
curl http://localhost:8080/series/category/Crime
```

## Tecnologias Utilizadas

- **Java 25**
- **Spring Web** (API REST) e **Spring Data JPA** (Hibernate)
- **PostgreSQL 17** (banco de dados relacional)
- **Jackson** (conversão de JSON)
- **Maven** (gerenciamento de dependências e build)
- **Docker / Docker Compose** (execução containerizada)
- **API OMDb** (fonte dos dados de séries e episódios)

## Licença

Este projeto está sob a licença MIT. Sinta-se livre para usar, modificar e distribuir.

---

Desenvolvido como projeto de estudo em Java
