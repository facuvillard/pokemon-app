# Pokemon App 🎮

A full-stack Pokemon application built as a technical challenge. Browse Pokemon from the PokeAPI, sync them to a local database, and manage your personal collection with custom fields.

## 🏗️ Architecture

```
pokemon-app/
├── poke-api/          # Java Spring Boot Backend
│   ├── src/
│   │   ├── main/java/com/pokemon/pokeapi/
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── service/       # Business logic
│   │   │   ├── dto/           # Java Records (request/response)
│   │   │   ├── model/         # JPA entities (Lombok)
│   │   │   ├── repository/    # Spring Data JPA
│   │   │   ├── config/        # Configuration
│   │   │   ├── security/      # JWT + Spring Security
│   │   │   ├── exception/     # Error handling
│   │   │   └── utils/         # Utilities
│   │   └── resources/
│   └── Dockerfile
├── poke-app/          # React Frontend (Mantine v7)
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── pages/         # Page components
│   │   ├── services/      # Axios API services
│   │   └── context/       # Auth context
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|-----------|---------|
| Java 21 | Language |
| Spring Boot 3.2 | Framework |
| Spring Security + JWT | Authentication |
| Spring Data JPA | Data access |
| SQLite | Local relational database |
| Caffeine Cache | PokeAPI response caching |
| Lombok | Boilerplate reduction (entities) |
| Java Records | DTOs (immutable) |
| JUnit 5 + Mockito | Testing |

### Frontend
| Technology | Purpose |
|-----------|---------|
| React 18 | UI library |
| Vite | Build tool |
| Mantine v7 | Component library |
| React Router v6 | Routing |
| Axios | HTTP client |
| @tabler/icons-react | Icons |

## 📋 User Stories

| # | Story | Description |
|---|-------|-------------|
| US01 | Pokemon Enumeration | Browse Pokemon with paginated results showing sprite, types, weight, and abilities |
| US02 | Detailed View | View comprehensive data: image, stats, description, and evolution chain |
| US03 | Data Synchronization | Sync Pokemon data to local SQLite database with custom fields |
| US04 | Local Data Modification | Update custom fields for locally stored Pokemon with proper validation |

## 🚀 Quick Start

### Option 1: Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/facuvillard/pokemon-app.git
cd pokemon-app

# Start both services
docker-compose up --build

# Backend: http://localhost:8080
# Frontend: http://localhost:3000
```

### Option 2: Local Development

#### Prerequisites
- Java 21
- Maven 3.9+
- Node.js 18+
- npm 9+

#### Backend
```bash
cd poke-api

# Build and run
mvn clean install
mvn spring-boot:run

# The API will be available at http://localhost:8080
```

#### Frontend
```bash
cd poke-app

# Install dependencies
npm install

# Start dev server
npm run dev

# The app will be available at http://localhost:5173
```

## 🔐 Default Credentials

The application comes pre-seeded with a demo user:

| Field | Value |
|-------|-------|
| Username | `admin` |
| Password | `admin123` |
| Email | `admin@pokemon.com` |

Additionally, the first 20 Pokemon (Bulbasaur to Raticate) are synced to the local database on first startup.

## 📡 API Endpoints

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/pokemon?page=0&size=20` | List Pokemon (paginated, from PokeAPI) |
| `GET` | `/api/v1/pokemon/{id}` | Get Pokemon detail (stats, description, evolution) |
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Login and receive JWT token |

### Protected Endpoints (JWT Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/local/pokemon/sync/{id}` | Sync a Pokemon to local database |
| `POST` | `/api/v1/local/pokemon/sync/batch` | Sync multiple Pokemon (body: `{"ids": [1,2,3]}`) |
| `GET` | `/api/v1/local/pokemon?page=0&size=20` | List locally synced Pokemon |
| `GET` | `/api/v1/local/pokemon/{id}` | Get local Pokemon detail |
| `PUT` | `/api/v1/local/pokemon/{id}` | Update custom fields |
| `DELETE` | `/api/v1/local/pokemon/{id}` | Delete local Pokemon |
| `GET` | `/api/v1/auth/me` | Get current user profile |

### Example: Update Pokemon Custom Fields
```bash
curl -X PUT http://localhost:8080/api/v1/local/pokemon/25 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "customName": "Pika",
    "region": "Kanto",
    "classificationTag": "Electric Mouse",
    "notes": "Ash'\''s partner Pokemon"
  }'
```

### Error Responses

| Status | Meaning |
|--------|---------|
| `400` | Bad Request — Invalid payload or validation error |
| `401` | Unauthorized — Missing or invalid JWT token |
| `404` | Not Found — Pokemon or resource not found |
| `500` | Internal Server Error |
| `502` | Bad Gateway — Error communicating with PokeAPI |

## 🧪 Running Tests

```bash
cd poke-api
mvn test
```

## 📁 Custom Fields (US03)

When a Pokemon is synced to the local database, users can add these custom fields:

| Field | Description | Max Length |
|-------|-------------|-----------|
| `customName` | Localized or personal name | 100 |
| `region` | Geographic metadata (e.g., "Kanto") | 50 |
| `classificationTag` | Internal classification tag | 50 |
| `notes` | Free-text notes or comments | 500 |

## 🐳 Docker

The project includes Dockerfiles for both services and a `docker-compose.yml` for easy orchestration:

```yaml
# Start everything
docker-compose up --build

# Start only backend
docker-compose up poke-api

# Stop all services
docker-compose down
```

## 📄 License

This project was created as a technical challenge exercise.
