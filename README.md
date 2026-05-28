# tax-payment-trainee

### Key Domain Concepts

| Concept          | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| `Invoice`        | Aggregate root containing principal, interest, penalty, and paid amounts.  |
| `Payment`        | Represents a tax payment with reference number, amount, and status.        |
| `Money`          | Value object enforcing currency consistency.                               |
| `PaymentStatus`  | Enum: `PENDING`, `SUCCESS`, `FAILED` with state transition logic.          |
| `InvoiceStatus`  | Tracks invoice lifecycle (e.g., `PAID`, `PARTIALLY_PAID`, `PENDING`).      |
| `TaxPeriod`      | Value object for tax reporting period (year + quarter/month).              |
| `TaxTypeCode`    | Domain code for different tax types (VAT, income tax, etc.).                |

## 🛠️ Tech Stack

- **Java 17** (or later)
- **Spring Boot** – Web, Data JPA, Validation
- **Hibernate / JPA** – Object‑relational mapping
- **Maven** – Build and dependency management
- **H2 / PostgreSQL** – In‑memory (dev) and production databases
- **JUnit & Mockito** – Unit and integration tests

## 📦 Getting Started

### Prerequisites
- JDK 17+
- Maven (or use the included Maven wrapper)

### Installation & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/josicoder1/tax-payment-trainee.git
   cd tax-payment-trainee
2. **Build the project**
   ```bash
   ./mvnw clean install
3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
