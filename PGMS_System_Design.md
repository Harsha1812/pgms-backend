<img width="446" height="649" alt="image" src="https://github.com/user-attachments/assets/bd58d3e6-8a0e-4fe6-bb96-a122a39ba45e" />🏢 PGMS – Paying Guest Management System

System Design & Architecture Document

1️⃣ Requirement Gathering
1.1 Functional Requirements (FR)
Core Business Features
Owner Features

Create & manage Businesses (PG brands)

Create & manage Branches

View financial summary (rent, expenses, profit)

Track occupancy

View branch-level reports

Manage branch managers

Track rent collection status

Branch Manager Features

View assigned branches

Manage rooms & beds

Onboard tenants

Allocate beds

Track rent payments

Manage expenses

View branch summary

Tenant Features

View rent dues

View payment history

Receive announcements

View branch contact details

Access via login (mobile-friendly)

Operational Features

Bed allocation management

Rent tracking (monthly)

Expense tracking (categorized)

Security deposit tracking

Role-based access control

Branch-level data isolation

1.2 Non-Functional Requirements (NFR)
Scalability & Low Latency

Must handle:

10–100 PGs initially

Each PG: 20–300 beds

API response < 300ms for dashboard

Efficient summary endpoints (no N+1 queries)

Use pagination for lists

Reliability & Durability

ACID-compliant transactions (PostgreSQL)

Rent collection must not duplicate (Idempotency support)

Bed allocation must prevent double assignment

Unique constraints enforced at DB level

Consistency

Strong consistency for:

Bed allocation

Rent payments

Expense entries

Dashboard aggregates computed server-side

No frontend calculations for financials

Global Availability (Future-Ready)

Stateless backend (Spring Boot)

JWT-based auth

Horizontally scalable app servers

Future-ready for load balancer + containerization

Fault Tolerance

Graceful error handling

Transaction rollback on failure

Retry mechanisms for future Kafka events

Idempotency support for POST APIs

Security

JWT-based authentication

Role-based access control (RBAC)

Owner isolation (ownerId enforced in queries)

Branch isolation (branchId scoping)

No tenant data exposure across owners

HTTPS enforced (production)

Observability

Structured logging

Request tracing

Centralized logs (future)

Metrics:

Rent collected

API latency

Active users

Occupancy %

1.3 Future Features

Automated late fee calculation

UPI / Razorpay integration

Tenant mobile app

NGO / Event collaboration system

Referral rewards

Notifications engine

Multi-product ecosystem (PGMS → AMS → HOMS)

Core platform dashboard (Founder view)

Unified identity service

2️⃣ BOE Calculations / Capacity Estimations
User Base (Initial 2 Years)

50 PG owners

10 branches per owner (max)

150 beds per branch (average)

Total beds:

50 × 10 × 150 = 75,000 beds

Assume 80% occupancy → 60,000 tenants

Monthly Data Volume
Rent Transactions

60,000 payments per month

Yearly:

60,000 × 12 = 720,000 payments
Storage Estimation
Tenants

~60,000 records

Payments

~1M rows in 1–2 years

Expenses

~10 per branch per month
50 × 10 × 10 × 12 = 60,000 per year

Total storage after 2 years:
~3–5 GB (very manageable)

Traffic Estimation

Daily Active Users: 5,000

Peak concurrent users: ~300

Average API calls per user session: 20

Peak RPS: ~50–100

This is very manageable with:

2–3 backend instances

PostgreSQL primary DB

3️⃣ Approach – High Level Design
3.1 Architecture Overview
🧱 Current Architecture

  flowchart LR
    User --> ReactApp
    ReactApp -->|REST| SpringBootAPI
    SpringBootAPI --> PostgreSQL
    SpringBootAPI --> JWTAuth

    <img width="895" height="353" alt="image" src="https://github.com/user-attachments/assets/9abc0690-478b-4fdf-9289-045fa8b4e838" />
  
  Logical Architecture 
  flowchart TD
    A[Frontend - React] --> B[API Layer - Spring Boot]
    B --> C[Service Layer]
    C --> D[Repository Layer]
    D --> E[(PostgreSQL DB)]

    <img width="473" height="549" alt="image" src="https://github.com/user-attachments/assets/cfff77d5-cd8a-4b2b-8fae-0a7a9e708cb7" />

  Domain Hierarchy

  flowchart TD
    Owner --> Business
    Business --> Branch
    Branch --> Room
    Room --> Bed
    Bed --> Tenant
    Tenant --> RentPayment

    <img width="446" height="649" alt="image" src="https://github.com/user-attachments/assets/7975485e-208e-4e30-a99a-ac4438ce6ee4" />

3.2 Data Consistency vs Real-Time Requirements

  | Feature        | Consistency Level | Reason                    |
| -------------- | ----------------- | ------------------------- |
| Bed Allocation | Strong            | Prevent double booking    |
| Rent Payment   | Strong            | Financial correctness     |
| Dashboard      | Near Real-Time    | Can tolerate slight delay |
| Occupancy Rate | Real-Time         | Derived from DB           |


3.3 Security & Privacy Considerations

Owner-level data isolation

Branch-level access restrictions

Tenant cannot access other tenant data

No data shared with third parties

One-way notification architecture (future)

JWT token expiry enforced

4️⃣ Databases & Rationale
PostgreSQL

Why?

ACID compliance

Strong relational consistency

Foreign key constraints

Mature indexing

Scales easily for early-stage SaaS

Schema Strategy

Each entity includes:

owner_id

created_by

updated_by

version (optimistic locking)

Prepares system for:

Auditability

Multi-tenant safety

Future scale

5️⃣ APIs (High-Level)

  Authentication
POST /api/v1/auth/login
Business APIs
GET    /businesses
POST   /businesses
GET    /businesses/{id}
Branch APIs
GET    /businesses/{businessId}/branches
POST   /businesses/{businessId}/branches
GET    /branches/{id}/summary
Room APIs
GET    /branches/{branchId}/rooms
POST   /branches/{branchId}/rooms
PUT    /rooms/{roomId}
Tenant APIs
POST   /tenants
GET    /tenants/{id}
Dashboard APIs
GET /dashboard/owner-summary
GET /dashboard/branch-summary


6️⃣ Deep Dive into Core Services
🛏 Bed Allocation Service
Responsibilities

Ensure bed not already allocated

Validate tenant active status

Record rent amount snapshot

Record deposit

Maintain allocation history

DB Constraint
UNIQUE (bed_id) WHERE is_active = true

Prevents double booking at DB level.

💰 Rent Tracking Service

Monthly rent expectation

Payment status

Partial payment support

Late fee (future)

Summary aggregation queries

💸 Expense Service

Categorized expense tracking

Branch-level isolation

Monthly aggregation

📊 Dashboard Aggregation Service

Should:

Use projection queries

Avoid loading entity graphs

Use optimized SUM + COUNT queries

Corner Cases & Solutions

| Problem                               | Solution                  |
| ------------------------------------- | ------------------------- |
| Double bed allocation                 | DB unique constraint      |
| Duplicate payment submission          | Idempotency key           |
| Owner accessing other owner's data    | owner_id filter           |
| Tenant leaving but bed still occupied | allocation_end_date logic |
| Partial payments                      | Track outstanding balance |


7️⃣ Addressing NFRs in Detail
Scalability

Stateless API

Ready for containerization

Horizontal scaling possible

DB indexing on:

owner_id

branch_id

bed_id

tenant_id

Reliability

Transactional boundaries

Optimistic locking

Retry for future async processing

Security

JWT

Role-based guards

Strict DB filtering

Observability

Future-ready for:

Prometheus

Grafana

ELK stack

Audit logs

🎯 Product Summary for PG Owners (Simple View)

PGMS helps you:

Track rent clearly

Know who hasn’t paid

Track expenses

Monitor occupancy

Onboard tenants digitally

Eliminate Excel chaos

🧠 Long-Term Evolution Path

Phase 1:

PGMS stable

10+ paying customers

Phase 2:

Event-driven architecture

Collaboration module

Phase 3:

Core platform (Founder Dashboard)

Multi-product ecosystem
  


