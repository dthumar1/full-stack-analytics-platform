# Analytics Platform

A full-stack analytics and automation platform for operational intelligence, built with Spring Boot (Java 17) and React (TypeScript).

## Features

- **Employee Management**: Track employee data, departments, and assignments
- **Operational Records**: Monitor productivity, error rates, and process metrics
- **Workflow Management**: Track tasks, deadlines, and assignments
- **Alert System**: Automated alerts for low productivity, high error rates, and deadline issues
- **Automation Rules**: Configurable automation for productivity checks and deadline monitoring
- **Analytics Dashboard**: Real-time metrics and trend analysis
- **CSV Import/Export**: Bulk data import and export capabilities

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Spring Security with JWT
- PostgreSQL 15
- Maven

### Frontend
- React 18
- TypeScript
- Vite
- React Router
- Axios
- Recharts
- Tailwind CSS

### Infrastructure
- Docker & Docker Compose
- GitHub Actions CI/CD
- Nginx

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)
- Node.js 18 (for local development)

### Using Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd windsurf-project-2

# Start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8080/api
# PostgreSQL: localhost:5432
```

### Local Development

#### Backend
```bash
cd backend
mvn spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

## Default Credentials

- **Email**: admin@analytics.local
- **Password**: Admin123!

## Database Setup

The database schema and sample data are automatically initialized when using Docker Compose. For manual setup:

```bash
# Connect to PostgreSQL
psql -h localhost -U analytics -d analytics_platform

# Run schema
\i database/schema.sql

# Run sample data
\i database/sample-data.sql
```

## API Documentation

### Authentication
All API endpoints (except `/api/auth/login`) require JWT authentication.

#### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@analytics.local",
  "password": "Admin123!"
}
```

### Main Endpoints

- `GET /api/employees` - List employees
- `GET /api/operational-records` - List operational records
- `GET /api/workflows` - List workflows
- `GET /api/alerts` - List alerts
- `GET /api/analytics/summary` - Dashboard summary
- `GET /api/automation/runs` - Automation runs
- `GET /api/departments` - List departments

See `docs/API.md` for detailed API documentation.

## Project Structure

```
windsurf-project-2/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/analytics/platform/
│   │       ├── controller/   # REST controllers
│   │       ├── service/      # Business logic
│   │       ├── repository/   # Data access
│   │       ├── entity/       # JPA entities
│   │       ├── dto/          # Request/response DTOs
│   │       ├── security/     # JWT & Spring Security
│   │       ├── exception/    # Exception handling
│   │       └── scheduler/    # Automation tasks
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── frontend/                # React application
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── api/            # API client
│   │   ├── types/          # TypeScript types
│   │   └── main.tsx
│   ├── package.json
│   └── vite.config.ts
├── database/                # SQL scripts
│   ├── schema.sql
│   └── sample-data.sql
├── docs/                   # Documentation
├── .github/workflows/      # CI/CD pipelines
└── docker-compose.yml
```

## Development

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend
npm test
```

### Building Docker Images
```bash
docker-compose build
```

## Deployment

See `docs/AWS_DEPLOYMENT.md` for AWS deployment instructions.

## License

MIT License
