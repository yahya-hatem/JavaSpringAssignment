# Bayt Al Hekma Library API Documentation

Complete API reference with examples and response formats.

## Base URL
```
http://localhost:8080/api/library
```

## Response Format

All responses follow a consistent JSON structure:

### Success Response
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { /* response data */ }
}
```

### Error Response
```json
{
  "success": false,
  "status": 400,
  "message": "Error description",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

---

## Catalogue Management

### 1. View All Items

**Request**
```http
GET /api/library/catalogue
```

**Response**
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "catalogueId": "BOOK001",
      "category": "Book",
      "title": "The Name of the Wind",
      "status": "AVAILABLE",
      "borrowerName": "-",
      "loanPeriodDays": 14,
      "oneDayFineCost": 5.0
    },
    {
      "catalogueId": "MAG001",
      "category": "Magazine",
      "title": "National Geographic",
      "status": "ON_LOAN",
      "borrowerName": "Ahmed Hassan",
      "loanPeriodDays": 7,
      "oneDayFineCost": 3.0
    },
    {
      "catalogueId": "DVD001",
      "category": "DVD",
      "title": "Inception",
      "status": "AVAILABLE",
      "borrowerName": "-",
      "loanPeriodDays": 3,
      "oneDayFineCost": 15.0
    }
  ]
}
```

### 2. View Items by Status

**Request**
```http
GET /api/library/items/status/ON_LOAN
```

**Valid Statuses**: `AVAILABLE`, `ON_LOAN`, `RESERVED`, `LOST`

**Response**
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "catalogueId": "MAG001",
      "category": "Magazine",
      "title": "National Geographic",
      "status": "ON_LOAN",
      "borrowerName": "Ahmed Hassan",
      "loanPeriodDays": 7,
      "oneDayFineCost": 3.0
    }
  ]
}
```

### 3. Search Item by ID

**Request**
```http
GET /api/library/items/BOOK001
```

**Response**
```json
{
  "success": true,
  "message": "OK",
  "data": {
    "catalogueId": "BOOK001",
    "category": "Book",
    "title": "The Name of the Wind",
    "status": "AVAILABLE",
    "borrowerName": "-",
    "loanPeriodDays": 14,
    "oneDayFineCost": 5.0
  }
}
```

**Error Response** (Item not found)
```json
{
  "success": false,
  "status": 404,
  "message": "Item with catalogue ID 'BOOK999' not found",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

### 4. Register Book

**Request**
```http
POST /api/library/items/books
Content-Type: application/json

{
  "catalogueId": "BOOK001",
  "title": "The Name of the Wind",
  "author": "Patrick Rothfuss",
  "pageCount": 662
}
```

**Response**
```json
{
  "success": true,
  "message": "Book registered successfully",
  "data": {
    "id": 1,
    "catalogueId": "BOOK001",
    "title": "The Name of the Wind",
    "status": "AVAILABLE",
    "author": "Patrick Rothfuss",
    "pageCount": 662
  }
}
```

### 5. Register Magazine

**Request**
```http
POST /api/library/items/magazines
Content-Type: application/json

{
  "catalogueId": "MAG001",
  "title": "National Geographic",
  "issueNumber": 45
}
```

**Response**
```json
{
  "success": true,
  "message": "Magazine registered successfully",
  "data": {
    "id": 2,
    "catalogueId": "MAG001",
    "title": "National Geographic",
    "status": "AVAILABLE",
    "issueNumber": 45
  }
}
```

### 6. Register DVD

**Request**
```http
POST /api/library/items/dvds
Content-Type: application/json

{
  "catalogueId": "DVD001",
  "title": "Inception",
  "runtime": 148
}
```

**Response**
```json
{
  "success": true,
  "message": "DVD registered successfully",
  "data": {
    "id": 3,
    "catalogueId": "DVD001",
    "title": "Inception",
    "status": "AVAILABLE",
    "runtime": 148
  }
}
```

---

## Member Management

### 1. View All Members

**Request**
```http
GET /api/library/members
```

**Response**
```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "membershipId": "M001",
      "name": "Ahmed Hassan",
      "category": "STUDENT",
      "balanceOwed": 25.0,
      "itemsHeld": 1
    },
    {
      "membershipId": "M002",
      "name": "Fatima Al-Rashid",
      "category": "STAFF",
      "balanceOwed": 0.0,
      "itemsHeld": 2
    }
  ]
}
```

### 2. Search Member by ID

**Request**
```http
GET /api/library/members/M001
```

**Response**
```json
{
  "success": true,
  "message": "OK",
  "data": {
    "membershipId": "M001",
    "name": "Ahmed Hassan",
    "category": "STUDENT",
    "balanceOwed": 25.0,
    "itemsHeld": 1
  }
}
```

**Error Response** (Member not found)
```json
{
  "success": false,
  "status": 404,
  "message": "Member with ID 'M999' not found",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

### 3. Register Member

**Request**
```http
POST /api/library/members
Content-Type: application/json

{
  "membershipId": "M001",
  "name": "Ahmed Hassan",
  "category": "STUDENT"
}
```

**Valid Categories**: `STUDENT` (25% waiver), `STAFF` (10% waiver), `PUBLIC` (0% waiver)

**Response**
```json
{
  "success": true,
  "message": "Member registered successfully",
  "data": {
    "id": 1,
    "membershipId": "M001",
    "name": "Ahmed Hassan",
    "category": "STUDENT",
    "balanceOwed": 0.0,
    "itemsHeld": 0
  }
}
```

---

## Circulation Operations

### 1. Borrow Item

**Request**
```http
POST /api/library/borrow
Content-Type: application/json

{
  "catalogueId": "BOOK001",
  "membershipId": "M001"
}
```

**Success Response**
```json
{
  "success": true,
  "message": "Item borrowed successfully",
  "loanPeriodDays": 14
}
```

**Error Responses**

Item not available:
```json
{
  "success": false,
  "status": 400,
  "message": "Item 'BOOK001' is not available (status: ON_LOAN)",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

Member holds maximum items:
```json
{
  "success": false,
  "status": 400,
  "message": "Member M001 already holds 3 items (maximum)",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

Member owes too much:
```json
{
  "success": false,
  "status": 400,
  "message": "Member M001 owes 150.5 EGP (exceeds 100 EGP limit)",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

### 2. Return Item and Process Fines

**Request**
```http
POST /api/library/return
Content-Type: application/json

{
  "catalogueId": "BOOK001",
  "daysOverdue": 5
}
```

**Response** (Book: 5 × 5.00 EGP = 25.00 EGP base fine)
```json
{
  "success": true,
  "message": "Item returned successfully",
  "fineDetails": {
    "itemId": "BOOK001",
    "membershipId": "M001",
    "daysOverdue": 5,
    "baseFine": 25.0,
    "waiverAmount": 6.25,
    "administrativeCharge": 10.0,
    "totalCharged": 28.75,
    "newBalance": 28.75
  }
}
```

**Fine Calculation Example**
- Book overdue 5 days with STUDENT member (25% waiver)
- Base fine: 5 × 5.00 = 25.00 EGP
- Waiver (25%): 25.00 × 0.25 = 6.25 EGP
- After waiver: 25.00 - 6.25 = 18.75 EGP
- Administrative charge: 10.00 EGP (never waived)
- **Total charged: 28.75 EGP**

**Magazine Return** (With fine cap)
```http
POST /api/library/return
Content-Type: application/json

{
  "catalogueId": "MAG001",
  "daysOverdue": 20
}
```

Response (Magazine: min(20 × 3.00, 30.00) = 30.00 EGP):
```json
{
  "success": true,
  "message": "Item returned successfully",
  "fineDetails": {
    "itemId": "MAG001",
    "membershipId": "M002",
    "daysOverdue": 20,
    "baseFine": 30.0,
    "waiverAmount": 3.0,
    "administrativeCharge": 10.0,
    "totalCharged": 37.0,
    "newBalance": 37.0
  }
}
```

**On-Time Return** (No fine)
```http
POST /api/library/return
Content-Type: application/json

{
  "catalogueId": "DVD001",
  "daysOverdue": 0
}
```

Response:
```json
{
  "success": true,
  "message": "Item returned successfully",
  "fineDetails": {
    "itemId": "DVD001",
    "membershipId": "M001",
    "daysOverdue": 0,
    "baseFine": 0.0,
    "waiverAmount": 0.0,
    "administrativeCharge": 0.0,
    "totalCharged": 0.0,
    "newBalance": 0.0
  }
}
```

### 3. Renew Loan

**Request**
```http
POST /api/library/renew
Content-Type: application/json

{
  "catalogueId": "BOOK001"
}
```

**Success Response**
```json
{
  "success": true,
  "message": "Loan renewed successfully",
  "renewalsRemaining": 1
}
```

**Error Responses**

Item cannot be renewed:
```json
{
  "success": false,
  "status": 400,
  "message": "Items of type 'DVD' cannot be renewed",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

Item not on loan:
```json
{
  "success": false,
  "status": 400,
  "message": "Item 'BOOK001' is not currently on loan",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

Renewal limit reached:
```json
{
  "success": false,
  "status": 400,
  "message": "Item 'BOOK001' has reached its renewal limit",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

---

## Item State Operations

### 1. Reserve Item

**Request**
```http
PUT /api/library/items/BOOK001/reserve
```

**Response**
```json
{
  "success": true,
  "message": "Item reserved successfully"
}
```

### 2. Mark Item Lost

**Request**
```http
PUT /api/library/items/BOOK001/lost
```

**Response**
```json
{
  "success": true,
  "message": "Item marked as lost"
}
```

### 3. Restore Item

**Request**
```http
PUT /api/library/items/BOOK001/restore
```

**Response**
```json
{
  "success": true,
  "message": "Item restored to available"
}
```

---

## Fine Payments

### Process Payment

**Request**
```http
POST /api/library/payments
Content-Type: application/json

{
  "membershipId": "M001",
  "amount": 25.0
}
```

**Success Response**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "newBalance": 3.75
}
```

**Error Responses**

Invalid amount (non-positive):
```json
{
  "success": false,
  "status": 400,
  "message": "Payment amount must be positive",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

Overpayment attempt:
```json
{
  "success": false,
  "status": 400,
  "message": "Payment amount exceeds balance owed (28.75 EGP)",
  "timestamp": "2024-01-15T10:30:45.123456"
}
```

---

## Reports

### Generate Library Report

**Request**
```http
GET /api/library/report?projectedOverduesDays=5
```

**Response**
```json
{
  "catalogueSize": 3,
  "itemsOnLoan": 1,
  "loanRate": 33.33,
  "totalOutstandingFines": 28.75,
  "projectedFines": 85.0
}
```

**Interpretation**
- **Catalogue Size**: 3 total items registered
- **Items on Loan**: 1 item currently borrowed
- **Loan Rate**: 33.33% of catalogue is currently out
- **Total Outstanding Fines**: Sum of all members' balances: 28.75 EGP
- **Projected Fines**: If all 1 item currently on loan was returned 5 days late:
  - MAGAZINE (5 × 3.00 = 15.00, capped at 30.00) + admin = up to 40 EGP
  - Calculation uses actual item types and amounts

---

## Error Codes

| Code | Meaning | Cause |
|------|---------|-------|
| 400 | Bad Request | Invalid operation (item not available, member not eligible, etc.) |
| 404 | Not Found | Item or member does not exist |
| 500 | Internal Server Error | Unexpected server error |

---

## Common Workflows

### Workflow 1: Borrow, Return On Time

1. POST `/api/library/borrow` → Loan period confirmed (e.g., 14 days)
2. POST `/api/library/return` with `daysOverdue: 0` → No fine charged

### Workflow 2: Borrow, Return Late, Pay Fine

1. POST `/api/library/borrow` → Loan period confirmed
2. POST `/api/library/return` with `daysOverdue: 3` → Fine calculated and charged
3. POST `/api/library/payments` → Member pays outstanding balance

### Workflow 3: Borrow, Renew, Return

1. POST `/api/library/borrow` → Item on loan
2. POST `/api/library/renew` → Renewals remaining confirmed
3. POST `/api/library/return` with `daysOverdue: 0` → Item returned

### Workflow 4: Search and Reserve

1. GET `/api/library/items/BOOK001` → Check item status
2. PUT `/api/library/items/BOOK001/reserve` → Reserve if available

---

## Rate Limiting & Pagination

Currently not implemented. Future enhancements will include:
- Rate limiting per member/IP
- Pagination for large result sets
- Request throttling

---

## Authentication & Authorization

Currently not implemented. Future enhancements will include:
- JWT authentication
- Role-based access control (Admin, Librarian, Member)
- API key management

---

## Testing with cURL

```bash
# View all items
curl http://localhost:8080/api/library/catalogue

# Register a book
curl -X POST http://localhost:8080/api/library/items/books \
  -H "Content-Type: application/json" \
  -d '{
    "catalogueId": "BOOK001",
    "title": "The Name of the Wind",
    "author": "Patrick Rothfuss",
    "pageCount": 662
  }'

# Register a member
curl -X POST http://localhost:8080/api/library/members \
  -H "Content-Type: application/json" \
  -d '{
    "membershipId": "M001",
    "name": "Ahmed Hassan",
    "category": "STUDENT"
  }'

# Borrow an item
curl -X POST http://localhost:8080/api/library/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "catalogueId": "BOOK001",
    "membershipId": "M001"
  }'

# Return an item (3 days late)
curl -X POST http://localhost:8080/api/library/return \
  -H "Content-Type: application/json" \
  -d '{
    "catalogueId": "BOOK001",
    "daysOverdue": 3
  }'

# Process payment
curl -X POST http://localhost:8080/api/library/payments \
  -H "Content-Type: application/json" \
  -d '{
    "membershipId": "M001",
    "amount": 25.0
  }'
```
