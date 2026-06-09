# tax-payment-trainee
# Tax Payment System 🏦
A comprehensive tax payment system built with **Domain-Driven Design (DDD)**, **Hexagonal Architecture**, **Transactional Outbox Pattern**, and **Apache Kafka** for reliable asynchronous event processing.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Payment Flow](#payment-flow)
- [Event-Driven Architecture](#event-driven-architecture)
- [Testing](#testing)
- [Monitoring & Debugging](#monitoring--debugging)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

This project demonstrates enterprise-grade software architecture patterns and practices:

- **Domain-Driven Design (DDD)**: Rich domain models with aggregates, entities, value objects, and domain events
- **Hexagonal Architecture**: Clean separation between business logic and infrastructure concerns
- **Transactional Outbox Pattern**: Reliable event publishing without dual-write problems
- **Event-Driven Architecture**: Asynchronous event processing with Apache Kafka
- **Payment Allocation**: Priority-based allocation (penalty → interest → principal)

### Key Features

✅ **Invoice Management** - Create, retrieve, list, and void tax invoices  
✅ **Payment Processing** - Handle invoice payments with priority-based allocation  
✅ **Event Publishing** - Reliable domain event publishing via Kafka  
✅ **Outbox Pattern** - Guaranteed at-least-once delivery with background polling  
✅ **Overpayment Prevention** - Domain-enforced business rules  
✅ **RESTful API** - Complete REST API with validation and error handling  
✅ **Docker Compose** - One-command infrastructure setup  
✅ **API Testing** - Complete Postman collection included  

---

## 🏗️ Architecture

### Hexagonal Architecture (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────────┐
│                         API Layer                            │
│  • REST Controllers (Invoice, Payment)                       │
│  • DTOs (Request/Response)                                   │
│  • Validation & Exception Handling                           │
└────────────────────────┬────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                   Application Layer                          │
│  • Use Cases (CreateInvoice, PayInvoice, VoidInvoice)       │
│  • Commands & Queries                                        │
│  • Inbound/Outbound Ports                                    │
└────────────────────────┬────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                             │
│  • Aggregates: Invoice, Payment                             │
│  • Value Objects: Money, TaxPeriod, TaxTypeCode             │
│  • Domain Events: InvoicePaidEvent, PaymentConfirmedEvent   │
│  • Domain Services: PaymentAllocationService                │
└──────────────────────────────────────────────────────────────┘
                         ↓
         ┌───────────────┴───────────────┐
         ↓                               ↓
┌──────────────────┐            ┌────────────────────┐
│  Persistence      │            │  Infrastructure    │
│  • JPA Adapters   │            │  • Kafka Publisher │
│  • Repositories   │            │  • Outbox Poller   │
│  • Mappers        │            │  • Event Serializer│
└──────────────────┘            └────────────────────┘
```
## Payment Flow Sequence

```
┌──────┐     ┌─────────┐     ┌─────────┐     ┌──────────┐     ┌─────────┐
│Client│     │   API   │     │ Outbox  │     │  Kafka   │     │  Bank   │
└──┬───┘     └────┬────┘     └────┬────┘     └────┬─────┘     └────┬────┘
   │              │               │               │                │
   │ POST /payments              │               │                │
   ├─────────────>│               │               │                │
   │              │ Save Payment  │               │                │
   │              │ + Outbox Entry│               │                │
   │              ├──────────────>│               │                │
   │              │               │               │                │
   │              │<──────────────┤               │                │
   │ 202 Accepted │               │               │                │
   │<─────────────┤               │               │                │
   │              │               │               │                │
   │              │               │ Poll & Publish│                │
   │              │               ├──────────────>│                │
   │              │               │               │ payment-requested
   │              │               │               ├───────────────>│
   │              │               │               │                │
   │              │               │               │  Process (3s)  │
   │              │               │               │                │
   │              │               │               │ payment-confirmed
   │              │               │               │<───────────────┤
   │              │               │               │                │
   │              │ Confirm Payment              │                │
   │              │ Allocate & Update            │                │
   │              │<──────────────────────────────┤                │
   │              │               │               │                │
   │ GET /payments/{id}          │               │                │
   ├─────────────>│               │               │                │
   │ 200 CONFIRMED               │               │                │
   │<─────────────┤               │               │                │
```

---

## 🛠️ Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Java | 21|
| **Framework** | Spring Boot | 4.0.6 |
| **Database** | PostgreSQL | 15 |
| **Messaging** | Apache Kafka | 7.5.0 (Confluent) |
| **ORM** | Spring Data JPA / Hibernate | 7.2.12 |
| **Build Tool** | Maven | 3.8+ |
| **Containerization** | Docker & Docker Compose | Latest |
| **API Testing** | Postman | - |
| **Serialization** | Jackson | 2.21.2 |

---

## 📁 Project Structure

```
tax-payment-trainee/
├── src/
│   ├── main/
│   │   ├── java/com/example/tax_payment/
│   │   │   ├── api/                          # REST Layer
│   │   │   │   ├── controller/               # REST Controllers
│   │   │   │   ├── dto/                      # Request/Response DTOs
│   │   │   │   │   ├── request/
│   │   │   │   │   └── response/
│   │   │   │   ├── exception/                # Exception Handlers
│   │   │   │   └── validation/               # Custom Validators
│   │   │   │
│   │   │   ├── application/                  # Application Layer
│   │   │   │   ├── command/                  # Commands
│   │   │   │   ├── query/                    # Queries
│   │   │   │   ├── result/                   # Results
│   │   │   │   ├── mapper/                   # Result Mappers
│   │   │   │   ├── service/                  # Use Case Implementations
│   │   │   │   └── port/
│   │   │   │       ├── inbound/              # Use Case Interfaces
│   │   │   │       └── outbound/             # Repository/Gateway Ports
│   │   │   │
│   │   │   ├── domain/                       # Domain Layer (DDD)
│   │   │   │   ├── model/                    # Aggregates & Entities
│   │   │   │   │   ├── Invoice.java
│   │   │   │   │   └── Payment.java
│   │   │   │   ├── valueobject/              # Value Objects
│   │   │   │   │   ├── Money.java
│   │   │   │   │   ├── TaxPeriod.java
│   │   │   │   │   └── TaxTypeCode.java
│   │   │   │   ├── event/                    # Domain Events
│   │   │   │   │   ├── DomainEvent.java
│   │   │   │   │   ├── InvoicePaidEvent.java
│   │   │   │   │   └── PaymentConfirmedEvent.java
│   │   │   │   ├── service/                  # Domain Services
│   │   │   │   │   └── PaymentAllocation.java
│   │   │   │   └── exception/                # Domain Exceptions
│   │   │   │
│   │   │   ├── infrastructure/               # Infrastructure Layer
│   │   │   │   ├── config/                   # Spring Configuration
│   │   │   │   │   ├── AppConfig.java
│   │   │   │   │   └── KafkaConfig.java
│   │   │   │   └── messaging/                # Kafka Integration
│   │   │   │       ├── KafkaEventPublisherAdapter.java
│   │   │   │       ├── OutboxPoller.java
│   │   │   │       └── EventSerializer.java
│   │   │   │
│   │   │   └── persistence/                  # Persistence Layer
│   │   │       ├── adapter/                  # Repository Adapters
│   │   │       │   ├── InvoiceRepositoryAdapter.java
│   │   │       │   ├── PaymentRepositoryAdapter.java
│   │   │       │   └── OutboxRepositoryAdapter.java
│   │   │       ├── entity/                   # JPA Entities
│   │   │       │   ├── InvoiceJpaEntity.java
│   │   │       │   ├── PaymentJpaEntity.java
│   │   │       │   └── OutboxEventJpaEntity.java
│   │   │       ├── repository/               # Spring Data Repositories
│   │   │       │   ├── SpringDataInvoiceRepository.java
│   │   │       │   ├── SpringDataPaymentRepository.java
│   │   │       │   └── SpringDataOutboxRepository.java
│   │   │       ├── mapper/                   # Entity Mappers
│   │   │       └── config/                   # Persistence Config
│   │   │
│   │   └── resources/
│   │       ├── application.properties        # Main configuration
│   │       ├── application-test.properties   # Test configuration
│   │       └──
│   │       
│   │
│   └── test/                                  # Test Layer
│       └── java/com/example/tax_payment/
│
├── docker-compose.yml                         # Infrastructure setup
├── Tax-Payment-API.postman_collection.json    # Postman collection
├── Tax-Payment-Local.postman_environment.json # Postman environment
├
├── pom.xml                                    # Maven dependencies
└── README.md                                  # This file
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** - [Download JDK](https://adoptium.net/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose** - [Download Docker](https://www.docker.com/get-started)
- **Postman** (optional) - [Download Postman](https://www.postman.com/downloads/)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd tax-payment-trainee
```

### 2. Start Infrastructure Services

Start PostgreSQL and Kafka using Docker Compose:

```bash
docker-compose up -d
```

Verify services are running:

```bash
docker-compose ps
```

Expected output:
```
NAME                    STATUS              PORTS
tax-payment-postgres    Up (healthy)        0.0.0.0:5432->5432/tcp
tax-payment-kafka       Up                  0.0.0.0:9092->9092/tcp
tax-payment-zookeeper   Up                  2181/tcp
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

Look for this log message:
```
Started TaxPaymentApplication in X.XXX seconds
```

### 5. Verify Setup

Test the application:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

### 6. Test with Postman

1. Open Postman
2. Import `Tax-Payment-API.postman_collection.json`
3. Import `Tax-Payment-Local.postman_environment.json`
4. Select "Tax Payment - Local" environment
5. Run "Create Invoice" request

---

## 📡 API Documentation

### Base URL

```
http://localhost:8080
```

### Endpoints

#### **Invoices**

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/invoices` | Create a new invoice |
| `GET` | `/api/invoices/{id}` | Get invoice by ID |
| `GET` | `/api/invoices?taxpayerTin={tin}` | List invoices by taxpayer |
| `PUT` | `/api/invoices/{id}/void` | Void an invoice |

#### **Payments**

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/payments` | Pay an invoice |

---

### Example Requests

#### 1. Create Invoice

```http
POST /api/invoices
Content-Type: application/json

{
  "taxpayerTin": "123456789",
  "taxTypeCode": "VAT",
  "taxYear": 2024,
  "taxMonth": 12,
  "principalAmount": 1000.00,
  "interestAmount": 50.00,
  "penaltyAmount": 25.00,
  "currency": "USD"
}
```

**Response (200 OK):**
```json
{
  "invoiceId": "6d44cd6d-7a92-4d5f-919b-b9062ecbaaf4",
  "taxpayerTin": "123456789",
  "taxType": "VAT",
  "taxPeriod": "2024-12",
  "status": "OPEN",
  "principalAmount": 1000.00,
  "interestAmount": 50.00,
  "penaltyAmount": 25.00,
  "paidPrincipal": 0.00,
  "paidInterest": 0.00,
  "paidPenalty": 0.00,
  "outstandingAmount": 1075.00,
  "currency": "USD"
}
```

#### 2. Pay Invoice

```http
POST /api/payments
Content-Type: application/json

{
  "invoiceId": "6d44cd6d-7a92-4d5f-919b-b9062ecbaaf4",
  "amount": 500.00,
  "currency": "USD"
}
```

**Response (200 OK):**
```json
{
  "paymentId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "referenceNumber": "REF-1780472748159",
  "status": "SUCCESS",
  "failureReason": null,
  "createdAt": "2024-06-03T09:15:48Z"
}
```

#### 3. Get Invoice Details

```http
GET /api/invoices/6d44cd6d-7a92-4d5f-919b-b9062ecbaaf4
```

**Response (200 OK):**
```json
{
  "invoiceId": "6d44cd6d-7a92-4d5f-919b-b9062ecbaaf4",
  "taxpayerTin": "123456789",
  "taxType": "VAT",
  "taxPeriod": "2024-12",
  "status": "PARTIALLY_PAID",
  "principalAmount": 1000.00,
  "interestAmount": 50.00,
  "penaltyAmount": 25.00,
  "paidPrincipal": 425.00,
  "paidInterest": 50.00,
  "paidPenalty": 25.00,
  "outstandingAmount": 575.00,
  "currency": "USD"
}
```

---

## 💰 Payment Flow

### Payment Allocation Rules (Priority Order)

Payments are allocated to invoice components in **strict priority order**:

1. **Penalty** (highest priority)
2. **Interest** (medium priority)
3. **Principal** (lowest priority)

### Example Allocation Scenarios

#### Scenario 1: Partial Payment

**Invoice:**
- Principal: $1,000.00
- Interest: $50.00
- Penalty: $25.00
- **Total Outstanding: $1,075.00**

**Payment: $500.00**

**Allocation:**
1. Penalty: $25.00 (fully paid) ✅
2. Interest: $50.00 (fully paid) ✅
3. Principal: $425.00 (partial payment)

**Result:**
- Outstanding Penalty: $0.00
- Outstanding Interest: $0.00
- Outstanding Principal: $575.00
- **Invoice Status: PARTIALLY_PAID**

---

#### Scenario 2: Full Payment

**Invoice:**
- Principal: $1,000.00
- Interest: $50.00
- Penalty: $25.00
- **Total Outstanding: $1,075.00**

**Payment: $1,075.00**

**Allocation:**
1. Penalty: $25.00 (fully paid) ✅
2. Interest: $50.00 (fully paid) ✅
3. Principal: $1,000.00 (fully paid) ✅

**Result:**
- All components paid
- **Invoice Status: PAID**
- **Domain Event: InvoicePaidEvent published**

---

#### Scenario 3: Overpayment Prevention

**Invoice:**
- Principal: $1,000.00
- Interest: $50.00
- Penalty: $25.00
- **Total Outstanding: $1,075.00**

**Payment: $2,000.00** ❌

**Result:**
- **Error: `OverPaymentException`**
- Payment rejected by domain logic
- Invoice state unchanged

**Business Rule:** The domain layer prevents overpayment by checking outstanding amounts for each component before allocation.

---

## 🔄 Event-Driven Architecture

### Transactional Outbox Pattern

This project implements the **Transactional Outbox Pattern** to guarantee reliable event publishing without dual-write problems.

#### How It Works

1. **Business Transaction**: When domain state changes (e.g., invoice paid), both the entity and domain event are saved in the same database transaction
2. **Outbox Table**: Events are stored in `outbox_events` table with `published = false`
3. **Background Poller**: Runs every 5 seconds, queries unpublished events
4. **Kafka Publishing**: Events are published to Kafka topics
5. **Mark Published**: Successfully published events are marked `published = true`

#### Event Topics

| Domain Event | Kafka Topic | Description |
|--------------|-------------|-------------|
| `InvoicePaidEvent` | `invoice-paid` | Invoice fully paid |
| `InvoicePartiallyPaidEvent` | `invoice-partially-paid` | Invoice partially paid |
| `PaymentConfirmedEvent` | `payment-confirmed` | Payment processed successfully |
| `PaymentRequestedEvent` | `payment-requested` | Payment requested |

#### Monitoring Events

**List Kafka Topics:**
```bash
docker exec -it tax-payment-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list
```

**Consume Events from Topic:**
```bash
docker exec -it tax-payment-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic invoice-paid \
  --from-beginning
```

**Check Outbox Table:**
```bash
docker exec -it tax-payment-postgres psql -U postgres -d tax_payment_trainee

# Query unpublished events
SELECT id, event_type, published, created_at, published_at
FROM outbox_events
WHERE published = false
ORDER BY created_at;

# Query all events
SELECT id, event_type, published, created_at, published_at
FROM outbox_events
ORDER BY created_at DESC
LIMIT 10;
```

---

## 🧪 Testing

### Quick Test Commands

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report

# Skip tests during build
mvn clean install -DskipTests
```

### Test with Postman

1. **Import Collection**: `Tax-Payment-API.postman_collection.json`
2. **Import Environment**: `Tax-Payment-Local.postman_environment.json`
3. **Run Tests**: Execute requests in order or use the Collection Runner

#### Recommended Test Flow

1. **Create Invoice** - Creates a standard invoice
2. **Get Invoice by ID** - Verifies invoice creation
3. **Pay Invoice - Partial Payment** - Makes partial payment
4. **Get Invoice by ID** - Check PARTIALLY_PAID status
5. **Pay Invoice - Full Payment** - Pays remaining balance
6. **Get Invoice by ID** - Verify PAID status



---

## 🔍 Monitoring & Debugging

### Application Logs

```bash
# Follow application logs
tail -f logs/application.log

# Check Kafka event publishing logs
tail -f logs/application.log | grep "KafkaEventPublisher"

# Check Outbox poller logs
tail -f logs/application.log | grep "OutboxPoller"
```

### Docker Logs

```bash
# View all service logs
docker-compose logs -f

# PostgreSQL logs
docker-compose logs -f postgres

# Kafka logs
docker-compose logs -f kafka

# Zookeeper logs
docker-compose logs -f zookeeper
```

### Database Inspection

```bash
# Connect to PostgreSQL
docker exec -it tax-payment-postgres psql -U postgres -d tax_payment

# View invoices
SELECT * FROM invoices ORDER BY id;

# View payments
SELECT * FROM payments ORDER BY created_at DESC;

# View outbox events
SELECT id, event_type, published, created_at, published_at 
FROM outbox_events 
ORDER BY created_at DESC;

# Exit
\q
```

### Kafka Monitoring

```bash
# List all topics
docker exec -it tax-payment-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list

# Describe a topic
docker exec -it tax-payment-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --describe \
  --topic invoice-paid

# Check consumer groups
docker exec -it tax-payment-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --list
```

---

## 🐛 Troubleshooting

### Application Won't Start

#### Problem: Port 8080 Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change application port in application.properties
server.port=8081
```

#### Problem: Multiple EventPublisher Beans

**Error:** `expected single matching bean but found 2: kafkaEventPublisherAdapter, simpleEventPublisherAdapter`

**Solution:** The `KafkaEventPublisherAdapter` should be marked with `@Primary` annotation (already fixed in current version).

---

### Docker Issues

#### Problem: Port 5432 Already in Use

```bash
# Stop system PostgreSQL
sudo systemctl stop postgresql

# Restart Docker Compose
docker-compose down
docker-compose up -d
```

#### Problem: Kafka Not Starting

```bash
# Check Zookeeper is running
docker-compose ps zookeeper

# Restart Kafka services
docker-compose restart zookeeper kafka

# Check logs
docker-compose logs kafka
```

#### Problem: "Version is obsolete" Warning

**Solution:** The `version` attribute has been removed from `docker-compose.yml` in the latest version.

---

### Database Issues

#### Problem: Tables Not Created

```bash
# Check Hibernate is creating tables
# In application.properties:
spring.jpa.hibernate.ddl-auto=update


#### Problem: Connection Refused

```bash
# Verify PostgreSQL is running
docker exec -it tax-payment-postgres pg_isready -U postgres

# Check connection settings in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tax_payment
spring.datasource.username=postgres
spring.datasource.password=postgres123
```

---

### Kafka Issues

#### Problem: Events Not Being Published

**Check Outbox Poller Configuration:**
```properties
# In application.properties
outbox.poller.enabled=true
outbox.poller.fixed-delay-ms=5000
outbox.poller.batch-size=100
```

**Check Active Profile:**
```bash
# Outbox poller is disabled in test profile
# Ensure you're not running with --spring.profiles.active=test
```

**Check Outbox Table:**
```bash
docker exec -it tax-payment-postgres psql -U postgres -d tax_payment_

SELECT * FROM outbox_events WHERE published = false;
```

#### Problem: Kafka Connection Timeout

```bash
# Verify Kafka is accessible
docker exec -it tax-payment-kafka kafka-broker-api-versions \
  --bootstrap-server localhost:9092

# Check advertised listeners in docker-compose.yml
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://kafka:29092
```

---

### External References

- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Transactional Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/reference/html/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

## 📝 License

This project is for educational purposes.

---

## 🎉 Project Status

### ✅ Completed Features

- ✅ **Domain Layer**: Complete DDD implementation with aggregates, value objects, and domain events
- ✅ **Application Layer**: All use cases (CreateInvoice, PayInvoice, VoidInvoice, GetInvoice, ListInvoices)
- ✅ **Persistence Layer**: JPA entities, repositories, and adapters for all domain aggregates
- ✅ **Infrastructure Layer**: Kafka integration, outbox pattern, event serialization, background polling
- ✅ **API Layer**: REST controllers with validation, exception handling, and comprehensive DTOs
- ✅ **Docker Compose**: PostgreSQL 15 + Kafka + Zookeeper setup
- ✅ **API Testing**: Complete Postman collection with test scenarios
- ✅ **Documentation**: Comprehensive README, Postman guide, design documents

### 🚀 Ready For

- ✅ Local development and testing
- ✅ API testing with Postman
- ✅ Event-driven integration testing
- ✅ Learning and demonstration purposes

---

**Built with ❤️ using Spring Boot, Kafka, and Domain-Driven Design**

**Happy Coding! 🚀**
