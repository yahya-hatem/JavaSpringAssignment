# Bayt Al Hekma Library Management System

A Spring Boot implementation of a comprehensive library management system based on the original Bayt Al Hekma specification. This application replaces a community librarian's wooden card index with digital circulation and fine tracking capabilities.

## Project Overview

### What This Program Does

The Bayt Al Hekma Library Management System performs two primary functions:

1. **Circulation Management**: Tracking a catalogue of borrowable items, who currently holds each one, their condition, and loan periods
2. **Fine Calculation**: Computing overdue charges, applying membership-based waivers, and tracking member debt

### Core Design Principle

> "The library holds items that are similar enough to be handled together, but different enough that each must be allowed to answer for itself."

The system is built around the idea that the code which loops over the catalogue never needs to know which specific kind of item it's holding. Every item can state how long it may be kept and what it costs to return late, but no two item types answer either question the same way.

## Architecture

### Technology Stack

- **Framework**: Spring Boot 2.7.x
- **Language**: Java 11+
- **Database**: H2 (development), easily configurable for production databases
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **API**: REST with JSON

### Project Structure

```
bayt-al-hekma-spring/
├── src/main/java/com/library/
│   ├── model/                 # Domain models
│   │   ├── ItemStatus.java
│   │   ├── MembershipType.java
│   │   ├── Renewable.java
│   │   ├── Member.java
│   │   ├── LibraryItem.java
│   │   ├── Book.java
│   │   ├── Magazine.java
│   │   └── DVD.java
│   ├── repository/           # Data access
│   │   ├── LibraryItemRepository.java
│   │   └── MemberRepository.java
│   ├── service/              # Business logic
│   │   └── LibraryService.java
│   ├── controller/           # REST endpoints
│   │   └── LibraryController.java
│   ├── dto/                  # Data transfer objects
│   │   ├── MemberDTO.java
│   │   ├── LibraryItemDTO.java
│   │   ├── FineResultDTO.java
│   │   └── LibraryReportDTO.java
│   ├── exception/            # Custom exceptions
│   │   ├── LibraryException.java
│   │   ├── ItemNotFoundException.java
│   │   ├── MemberNotFoundException.java
│   │   └── InvalidOperationException.java
│   ├── config/               # Spring configuration
│   │   └── GlobalExceptionHandler.java
│   └── BaytAlHekmaApplication.java
├── src/main/resources/
│   └── application.yml       # Configuration
├── pom.xml                   # Maven dependencies
└── README.md                 # This file
```

## Building and Running

### Prerequisites

- Java 11 or higher
- Maven 3.6+

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Catalogue Operations

#### View All Items
```
GET /api/library/catalogue
```

#### View Items by Status
```
GET /api/library/items/status/{status}
Statuses: AVAILABLE, ON_LOAN, RESERVED, LOST
```

#### Search Item by ID
```
GET /api/library/items/{catalogueId}
```

#### Register Book
```
POST /api/library/items/books
{
  "catalogueId": "BOOK001",
  "title": "The Name of the Wind",
  "author": "Patrick Rothfuss",
  "pageCount": 662
}
```

#### Register Magazine
```
POST /api/library/items/magazines
{
  "catalogueId": "MAG001",
  "title": "National Geographic",
  "issueNumber": 45
}
```

#### Register DVD
```
POST /api/library/items/dvds
{
  "catalogueId": "DVD001",
  "title": "Inception",
  "runtime": 148
}
```

### Member Operations

#### View All Members
```
GET /api/library/members
```

#### Search Member by ID
```
GET /api/library/members/{membershipId}
```

#### Register Member
```
POST /api/library/members
{
  "membershipId": "M001",
  "name": "Ahmed Hassan",
  "category": "STUDENT"  // STUDENT, STAFF, or PUBLIC
}
```

### Circulation Operations

#### Borrow Item
```
POST /api/library/borrow
{
  "catalogueId": "BOOK001",
  "membershipId": "M001"
}
```

#### Return Item
```
POST /api/library/return
{
  "catalogueId": "BOOK001",
  "daysOverdue": 3
}
```

#### Renew Loan
```
POST /api/library/renew
{
  "catalogueId": "BOOK001"
}
```

### Item State Operations

#### Reserve Item
```
PUT /api/library/items/{catalogueId}/reserve
```

#### Mark Item Lost
```
PUT /api/library/items/{catalogueId}/lost
```

#### Restore Item
```
PUT /api/library/items/{catalogueId}/restore
```

### Fine Operations

#### Process Payment
```
POST /api/library/payments
{
  "membershipId": "M001",
  "amount": 50.00
}
```

### Reports

#### Generate Library Report
```
GET /api/library/report?projectedOverduesDays=5
```

## Domain Model

### Item Types

| Item | Loan Period | Fine Rate | Cap | Renewable |
|------|------------|-----------|-----|-----------|
| Book | 14 days | 5.00 EGP/day | None | Yes (2x) |
| Magazine | 7 days | 3.00 EGP/day | 30.00 EGP | Yes (1x) |
| DVD | 3 days | 15.00 EGP/day | None | No |

### Item Status

- `AVAILABLE`: Available for borrowing
- `ON_LOAN`: Currently borrowed by a member
- `RESERVED`: Reserved by a member
- `LOST`: Marked as lost

### Membership Categories

| Category | Waiver | Fine Cap |
|----------|--------|----------|
| STUDENT | 25% | None |
| STAFF | 10% | None |
| PUBLIC | 0% | None |

### Borrowing Eligibility

A member may borrow only if they:
- Hold fewer than 3 items
- Owe no more than 100.00 EGP

## Business Rules

### Catalogue Rules
1. Catalogue IDs are unique
2. A catalogue ID and title never change once the item is created
3. Lending is only possible from the AVAILABLE state
4. Returning is only possible from the ON_LOAN state
5. Returning always resets the state, borrower, and renewal count

### Member Rules
6. Membership IDs are unique
7. A membership ID and category never change
8. A member may hold at most 3 items at once
9. A member owing more than 100.00 EGP cannot borrow
10. Balance and item counts move only through controlled paths

### Fine Rules
11. Days overdue must not be negative
12. Zero days overdue means nothing is charged (including no administrative charge)
13. The magazine fine never exceeds 30.00 EGP
14. The waiver applies to the base fine only, never to the administrative charge (10.00 EGP)
15. The member must be identified from the item before the item is freed

### Renewal Rules
16. Only item types carrying the Renewable capability can be renewed
17. An item can only be renewed while it is on loan
18. An item cannot be renewed beyond its type's limit

## Key Design Patterns

### Polymorphism Without Type Checking

The `LibraryItem` abstract class defines three abstract methods that every subclass must answer:
- `calculateOverdueFine(int daysOverdue)` - How much does this cost when late?
- `getLoanPeriodDays()` - How long may this be kept?
- `getCategory()` - What is this item called?

This allows the library to work with items without knowing their specific types, implementing the Strategy pattern.

### Capability Interface

The `Renewable` interface represents the capability to renew a loan. Only Books and Magazines implement this; DVDs do not. This separates "being a library item" from "being renewable", allowing DVDs to be perfectly ordinary catalogue items while remaining impossible to renew.

### Immutable Core Fields

Certain fields (catalogue ID, title, membership ID, category) never change once created. This is enforced at the model level, not just the service level, making invalid states impossible to construct.

### Controlled State Transitions

The `Member` class never allows direct modification of balance or item count. These values move only through specific paths:
- `chargeFine()` - increases balance
- `acceptPayment()` - decreases balance
- `recordBorrowing()` - increases item count
- `recordReturn()` - decreases item count

This ensures data integrity and makes the system's rules explicit in code.

## Extension Points

To add a new item type:

1. Create a new class extending `LibraryItem`
2. Implement the three abstract methods
3. If renewable, implement the `Renewable` interface
4. Add a new endpoint in `LibraryController` to register this type

Example:
```java
@Entity
@DiscriminatorValue("AUDIOBOOK")
public class AudioBook extends LibraryItem implements Renewable {
    // Implementation
}
```

## Testing

Run the test suite:
```bash
mvn test
```

## Configuration

### Database

The default configuration uses H2 in-memory database. To use PostgreSQL or MySQL:

1. Add the appropriate driver dependency to `pom.xml`
2. Update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bayt_al_hekma
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQL10Dialect
    hibernate:
      ddl-auto: validate
```

### Administrative Charge

The administrative charge is hardcoded to 10.00 EGP in `LibraryService`. To make it configurable:

```java
@Value("${library.admin-charge:10.0}")
private double administrativeCharge;
```

Then add to `application.yml`:
```yaml
library:
  admin-charge: 10.0
```

## Performance Considerations

- The H2 database includes indices on `catalogueId` and `membershipId` for fast lookups
- The `LibraryReportDTO` calculation uses `stream()` for flexibility; for large catalogues, consider SQL aggregation
- Use pagination for large result sets (future enhancement)

## License

This implementation follows the original Bayt Al Hekma specification.
