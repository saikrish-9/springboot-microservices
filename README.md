# Spring Boot Microservices Project

A comprehensive microservices architecture built with Spring Boot, featuring multiple independent services, an API Gateway, and an Angular frontend. This project demonstrates modern microservices patterns including service-to-service communication, event-driven architecture, security, and distributed tracing.

## 📋 Project Overview

This is a complete e-commerce microservices solution with the following components:

- **API Gateway**: Central entry point for all client requests with JWT/OAuth2 authentication
- **Product Service**: Manages product catalog (MongoDB)
- **Order Service**: Handles order processing and management (MySQL, Kafka)
- **Inventory Service**: Manages stock levels (MySQL)
- **Notification Service**: Sends notifications via email and other channels (Kafka consumer)
- **Frontend**: Angular 18 application for user interaction

## 🏗️ Architecture

```
┌─────────────────┐
│   Angular App   │
│   (Frontend)    │
└────────┬────────┘
         │
         ▼
┌──────────────────────┐
│   API Gateway        │
│ (Spring Cloud Gateway)│
│ (JWT/OAuth2)         │
└────────┬─────────────┘
         │
    ┌────┼────────┬────────────┐
    ▼    ▼        ▼            ▼
┌─────┐ ┌──────┐ ┌───────┐ ┌──────────────┐
│Order│ │Product│ │Inventory│ │Notification│
│Service│ │Service│ │Service│ │Service│
└─────┘ └──────┘ └───────┘ └──────────────┘
  │ ▲      │        │         ▲  │
  │ │      │        │         │  │
  └─┼──────┼────────┼─────────┘  │
    └──────────────────────────────┘
        Kafka Message Broker
```

## 🚀 Technologies & Stack

### Backend Services
- **Spring Boot**: 4.0.3 & 3.3.5
- **Java**: 17 & 21
- **Spring Cloud**: 2025.1.0 & 2023.0.3
- **Spring Data JPA**: ORM for relational databases
- **Spring Data MongoDB**: Document database support
- **Spring Kafka**: Event streaming
- **Spring Security**: OAuth2/JWT authentication
- **SpringDoc OpenAPI**: Swagger/API documentation

### Databases
- **MySQL 8.x**: Relational data (Orders, Inventory)
- **MongoDB**: Document storage (Products)

### Infrastructure & Tools
- **Docker & Docker Compose**: Containerization and orchestration
- **Kafka & Zookeeper**: Message streaming
- **Keycloak**: Identity and access management (OAuth2/OIDC)
- **Grafana**: Metrics visualization
- **Prometheus**: Metrics collection
- **Tempo**: Distributed tracing
- **Flyway**: Database migrations

### Frontend
- **Angular 18**: Modern frontend framework
- **TypeScript**: Type-safe programming
- **RxJS**: Reactive programming
- **Tailwind CSS**: Utility-first CSS framework
- **angular-auth-oidc-client**: OpenID Connect authentication

## 📁 Project Structure

```
SpringBoot Microservices/
├── api-gateway/              # Spring Cloud Gateway service
│   ├── src/main/java/
│   ├── docker/
│   │   ├── keycloak/        # Keycloak configuration
│   │   ├── grafana/         # Grafana datasources
│   │   ├── prometheus/      # Prometheus config
│   │   └── tempo/           # Tempo tracing config
│   ├── docker-compose.yml   # Keycloak + MySQL setup
│   └── pom.xml
│
├── product-service/          # Product Management Service
│   ├── src/main/java/
│   ├── data/mongodb/        # MongoDB data volume
│   ├── docker-compose.yml
│   └── pom.xml
│
├── order-service/            # Order Processing Service
│   ├── src/main/java/
│   ├── data/mysql/          # MySQL data volume
│   ├── docker/mysql/        # MySQL initialization scripts
│   ├── docker-compose.yml   # MySQL + Kafka setup
│   └── pom.xml
│
├── inventory-service/        # Inventory Management Service
│   ├── src/main/java/
│   ├── src/main/resources/  # Flyway migrations
│   ├── docker-compose.yml
│   └── pom.xml
│
├── notification-service/     # Notification Service (Kafka Consumer)
│   ├── src/main/java/
│   └── pom.xml
│
├── frontend/                 # Angular Application
│   ├── src/
│   │   ├── app/
│   │   │   ├── config/      # Auth configuration
│   │   │   ├── interceptor/ # HTTP interceptors
│   │   │   ├── pages/       # Page components
│   │   │   ├── services/    # API services
│   │   │   └── shared/      # Shared components
│   │   ├── main.ts
│   │   ├── index.html
│   │   └── styles.css
│   ├── package.json
│   └── tailwind.config.js
│
└── README.md
```

## 🛠️ Prerequisites

- **Java**: JDK 17+ (for API Gateway) & JDK 21 (for other services)
- **Maven**: 3.6+
- **Node.js**: 18+ and npm 9+
- **Docker**: 20.10+ and Docker Compose
- **Git**: For version control

## ⚙️ Configuration

### API Gateway
- **Port**: 8080
- **OAuth2 Provider**: Keycloak
- **Routes**: Configured to forward requests to backend services

### Services
| Service | Port | Database | Key Features |
|---------|------|----------|--------------|
| Product Service | 8001 | MongoDB | REST API, OpenAPI docs |
| Order Service | 8002 | MySQL | REST API, Kafka producer, OpenAPI docs |
| Inventory Service | 8003 | MySQL | REST API, Flyway migrations, OpenAPI docs |
| Notification Service | 8004 | - | Kafka consumer, Email integration |

### Frontend
- **Port**: 4200
- **Auth**: OpenID Connect via Keycloak
- **API Base URL**: Proxied through API Gateway

### Authentication (Keycloak)
- **Port**: 8181
- **Default Admin**: admin/admin
- **Database**: MySQL (keycloak db)

## 🏃 Getting Started

### 1. Clone the Repository

```bash
cd /Users/saikrishna/Desktop/"SpringBoot Microservices"
git clone <repository-url>
```

### 2. Start Infrastructure Services

#### Start Keycloak & Gateway Dependencies
```bash
cd api-gateway
docker-compose up -d
```

This starts:
- Keycloak (Port 8181)
- MySQL for Keycloak (Port 3306)
- Grafana (Port 3000)
- Prometheus (Port 9090)

#### Start Order Service & Message Broker
```bash
cd ../order-service
docker-compose up -d
```

This starts:
- MySQL (Port 3306)
- Kafka Broker (Port 9092)
- Zookeeper (Port 2181)

#### Start Product Service
```bash
cd ../product-service
docker-compose up -d
```

This starts:
- MongoDB

#### Start Inventory Service
```bash
cd ../inventory-service
docker-compose up -d
```

This starts:
- MySQL

### 3. Build Backend Services

#### API Gateway (Java 17)
```bash
cd api-gateway
mvn clean package
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

#### Product Service (Java 21)
```bash
cd product-service
mvn clean package
java -jar target/product-service-0.0.1-SNAPSHOT.jar
```

#### Order Service (Java 21)
```bash
cd order-service
mvn clean package
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

#### Inventory Service (Java 21)
```bash
cd inventory-service
mvn clean package
java -jar target/inventory-service-0.0.1-SNAPSHOT.jar
```

#### Notification Service (Java 21)
```bash
cd notification-service
mvn clean package
java -jar target/notification-service-0.0.1-SNAPSHOT.jar
```

### 4. Start Frontend

```bash
cd frontend
npm install
npm start
```

The application will be available at `http://localhost:4200`

## 📚 API Documentation

Once services are running, access their Swagger/OpenAPI documentation:

- **API Gateway**: http://localhost:8080/swagger-ui.html
- **Product Service**: http://localhost:8001/swagger-ui.html
- **Order Service**: http://localhost:8002/swagger-ui.html
- **Inventory Service**: http://localhost:8003/swagger-ui.html

## 🔐 Authentication & Security

- **OAuth2 Provider**: Keycloak
- **Admin Console**: http://localhost:8181
- **Default Credentials**: admin / admin
- **JWT Token**: Automatically handled by frontend via OIDC
- **API Gateway**: Validates JWT tokens and forwards authenticated requests

### Setup Keycloak Realm

1. Access Keycloak admin console at http://localhost:8181
2. Create a new realm or import from `api-gateway/docker/keycloak/realms/`
3. Create OIDC client for Angular frontend
4. Configure redirect URIs: `http://localhost:4200/callback`
5. Update `frontend/src/app/config/auth-config.ts` with credentials

## 📊 Monitoring & Observability

- **Prometheus**: http://localhost:9090 - Metrics collection
- **Grafana**: http://localhost:3000 - Visualization and dashboards
- **Tempo**: Distributed tracing backend
- **Springdoc OpenAPI**: REST API documentation

## 🔄 Inter-Service Communication

### Synchronous
- REST calls between services via API Gateway

### Asynchronous
- **Kafka Topics**: Event-driven communication
- Order Service publishes order events
- Notification Service consumes and sends notifications

## 📝 Database Migrations

### Inventory Service (Flyway)
Migrations are automatically applied on startup from `src/main/resources/db/migration/`

### Order Service & MySQL
Initialize with custom SQL scripts in `docker/mysql/init.sql`

## 🧪 Testing

### Backend
```bash
# Run tests for a service
cd <service-name>
mvn test
```

### Frontend
```bash
cd frontend
npm test
```

## 🐳 Docker Support

Each service has Docker support. Build images:

```bash
# Build individual service images
cd api-gateway
docker build -t api-gateway:1.0 .

cd ../product-service
docker build -t product-service:1.0 .

# Similar for other services
```

## 📦 Deployment

### Local Kubernetes (Optional)
Services can be deployed to Kubernetes with appropriate manifests.

### Docker Compose Production Setup
Combine all services in a master `docker-compose.yml` for full stack deployment.

## 🐛 Troubleshooting

### Port Conflicts
If ports are already in use, modify the `docker-compose.yml` files and service configuration.

### Database Connection Issues
- Ensure all Docker containers are running: `docker ps`
- Check service logs: `docker logs <container-name>`
- Verify network connectivity: `docker network ls`

### Kafka Connection Issues
- Wait a few seconds for Kafka to be fully ready
- Check Kafka broker logs: `docker logs broker`

### Keycloak Not Starting
- Ensure MySQL container is healthy
- Check Keycloak logs: `docker logs keycloak`
- Verify port 8181 is free

## 📖 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Angular Documentation](https://angular.io/docs)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Docker Compose](https://docs.docker.com/compose/)

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Run tests
4. Submit a pull request

## 📄 License

This project is provided as-is for educational purposes.

## 👥 Authors

Sai Krishna

---

**Last Updated**: March 6, 2026

For issues or questions, please contact the development team.

