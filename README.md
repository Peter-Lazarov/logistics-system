# Architecture

## authentication-service
Service responsible for authentication and user management.

Responsibilities:
- User registration
- User login
- JWT access token generation
- JWT refresh token generation
- User profile management
- User status management (ACTIVE / DISABLED)
- Token validation for downstream services
---

## common-service
Service responsible for shared business entities.

Domains:
- Clients
- Drivers
- Routes

Provides reference data used by shipment processing.
---

## shipment-service
Core logistics service.

Responsibilities:
- Shipment creation
- Shipment lifecycle management
- Driver assignment
- Route assignment
- Shipment history
- Shipment workflow execution

Shipment lifecycle:
```text
CREATED
→ ASSIGNED
→ LOADED
→ IN_TRANSIT
→ ARRIVED
→ DELIVERED
```
---

## mock-gps-tracker
Utility application used for GPS simulation.

Responsibilities:
- Generate vehicle position data
- Simulate moving vehicles
- Send GPS coordinates to tracking-api
---

## tracking-api
Ingress service for GPS data.
Responsibilities:
- Receive location updates
- Validate incoming tracking data
- Normalize GPS payloads
- Forward data to tracking-service
---

## tracking-service
Tracking storage and visualization backend.

Responsibilities:
- Store GPS coordinates
- Maintain tracking history
- Provide tracking data for map visualization
- Serve vehicle movement information



# Logistics System
Microservice-based logistics platform for shipment management, route assignment, driver management and vehicle tracking.

## Architecture
### authentication-service

Service responsible for authentication and user management.

Responsibilities:
- User registration
- User login
- JWT access token generation
- JWT refresh token generation
- User profile management
- User status management (ACTIVE / DISABLED)
- Token validation for downstream services

---

### common-service
Service responsible for shared business entities.

Domains:
- Clients
- Drivers
- Routes

Provides reference data used by shipment processing.

---

### shipment-service
Core logistics service.

Responsibilities:
- Shipment creation
- Shipment lifecycle management
- Driver assignment
- Route assignment
- Shipment history
- Shipment workflow execution

Shipment lifecycle:
```text
CREATED
→ ASSIGNED
→ LOADED
→ IN_TRANSIT
→ ARRIVED
→ DELIVERED
```

---
### mock-gps-tracker
Utility application used for GPS simulation.

Responsibilities:
- Generate vehicle position data
- Simulate moving vehicles
- Send GPS coordinates to tracking-api
---

### tracking-api
Ingress service for GPS data.

Responsibilities:
- Receive location updates
- Validate incoming tracking data
- Normalize GPS payloads
- Forward data to tracking-service

---

### tracking-service
Tracking storage and visualization backend.

Responsibilities:
- Store GPS coordinates
- Maintain tracking history
- Provide tracking data for map visualization
- Serve vehicle movement information

## Technologies
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Angular
- PostgreSQL
- MongoDB
- RabbitMQ
- Docker
- AWS

## Databases
| Service | Database |
|----------|----------|
| authentication-service | PostgreSQL |
| common-service | PostgreSQL |
| shipment-service | PostgreSQL |
| tracking-service | MongoDB |

## User Roles
### CLIENT
- Create shipments
- View personal shipments
- Manage profile

### EMPLOYEE
- Manage shipments
- Assign drivers
- Assign routes
- Process shipment workflow

### ADMIN
- Manage users
- Create employees
- Enable / Disable users
- Full system access

## Shipment Workflow
```text
CREATED
→ ASSIGNED
→ LOADED
→ IN_TRANSIT
→ ARRIVED
→ DELIVERED
```

Additional transitions:
```text
DELAYED
CANCELLED
```

## Tracking Flow
```text
mock-gps-tracker
        ↓
tracking-api
        ↓
tracking-service
        ↓
MongoDB
        ↓
Map Visualization
```

## Local Development
### Prerequisites
- Java 21
- Node.js
- PostgreSQL
- MongoDB
- RabbitMQ
- Maven

### Environment Variables
Configuration is provided through environment variables.

Load variables from the project root:
```bash
set -a
source .env
set +a
```

### Running Services
Authentication Service
```bash
cd authentication-service
mvn spring-boot:run
```

Common Service
```bash
cd common-service
mvn spring-boot:run
```

Shipment Service
```bash
cd shipment-service
mvn spring-boot:run
```

Tracking API
```bash
cd tracking-api
mvn spring-boot:run
```

Tracking Service
```bash
cd tracking-service
mvn spring-boot:run
```

Frontend
```bash
cd frontend
npm install
ng serve
```


## Docker Deployment

Build and start all services:
```bash
docker compose up --build
```
Run in background:
```bash
docker compose up -d
```
Stop all services:
```bash
docker compose down
```

Rebuild containers:
```bash
docker compose build
```

## AWS Deployment
```bash
git clone <repository-url>
cd logistics-system
docker compose up -d
```

Configuration is provided through environment variables.

## Default Ports
| Service | Port |
|----------|----------|
| Frontend | 4200 |
| Authentication Service | 8080 |
| Common Service | 8081 |
| Shipment Service | 8082 |
| Tracking API | 8083 |
| Tracking Service | 8084 |
| RabbitMQ Management | 15672 |

## Future Improvements
- OpenStreetMap shipment tracking
- WebSocket live vehicle tracking
- Automated CI/CD pipeline
- Docker image registry
- AWS infrastructure automation
- Tracking dashboard
- Notifications


## Access
### Frontend
```text
http://localhost:4200
```

### Default Admin Account
```text
Email: admin@sunny-times.com
Password: 1234
```

### User Roles
| Role | Permissions |
|--------|--------|
| CLIENT | Create and view shipments |
| EMPLOYEE | Manage shipments, assign drivers and routes |
| ADMIN | Full system administration and user management |

## Main Features
### Authentication
- User registration
- User login
- JWT authentication
- Refresh token support
- Profile management
- User activation and deactivation
- Role-based authorization

### Client Management
- Create clients
- Manage client information
- View client details

### Driver Management
- Create drivers
- Assign vehicles
- Manage driver information
- View driver details

### Route Management
- Create routes
- Manage route information
- View route details

### Shipment Management
- Create shipments
- Assign drivers
- Assign routes
- View shipment details
- Track shipment history

### Shipment Workflow
```text
CREATED
→ ASSIGNED
→ LOADED
→ IN_TRANSIT
→ ARRIVED
→ DELIVERED
```

Additional states:
```text
DELAYED
CANCELLED
```

### Tracking
- GPS tracking simulation
- Coordinate collection
- Tracking history storage
- Vehicle movement visualization (planned)

### Administration
- Create employees
- Manage users
- Activate users
- Disable users
- View system users

## Project Goals
The purpose of the project is to simulate a modern logistics platform built with a microservice architecture.

The system supports:
- User authentication and authorization
- Shipment creation and management
- Driver and route assignment
- Shipment lifecycle processing
- GPS tracking integration
- Role-based access control
- Containerized deployment with Docker
- Cloud deployment with AWS

## System Flow
### Authentication Flow
```text
Client
    ↓
Authentication Service
    ↓
JWT Access Token
    ↓
Protected Services
```

### Shipment Flow
```text
Create Shipment
       ↓
Assign Driver
       ↓
Load Shipment
       ↓
Start Route
       ↓
Arrive
       ↓
Deliver
```

### Tracking Flow
```text
Mock GPS Tracker
        ↓
Tracking API
        ↓
Tracking Service
        ↓
MongoDB
        ↓
Map Visualization
```

## Project Structure
```text
logistics-system
│
├── api-contracts
├── authentication-service
├── common-service
├── shipment-service
├── tracking-api
├── tracking-service
├── mock-gps-tracker
├── frontend
├── shared-security
│
├── docker-compose.yml
├── .env
└── README.md
```

## Current Status
Implemented:
- Authentication
- Authorization
- JWT Security
- User Management
- Client Management
- Driver Management
- Route Management
- Shipment Workflow
- Shipment History
- Tracking Infrastructure
- Angular Frontend
- Role-Based Access Control
- Docker Configuration

Planned:
- OpenStreetMap Tracking Visualization
- Live Tracking Dashboard
- AWS Deployment
- CI/CD Pipeline

## Notes
This project was developed as a learning and portfolio project focused on:

- Microservice architecture
- Spring Boot ecosystem
- Angular frontend development
- JWT authentication and authorization
- Event-driven communication
- GPS tracking integration
- Docker containerization
- AWS deployment

The goal was to build a realistic logistics platform covering the complete shipment lifecycle, from shipment creation to delivery and vehicle tracking.

## Author
Peter
Assisted by Copilot

Technologies and architectural decisions were implemented for learning purposes while following common industry practices for microservice-based applications.
