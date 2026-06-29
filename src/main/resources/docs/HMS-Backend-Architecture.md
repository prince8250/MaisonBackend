# Hostel Management System
## Backend Architecture Design

**Technology Stack:** Java · Spring Boot · Spring Data JPA · PostgreSQL
**Version:** 1.0

---

## 1. Overview

The Hostel Management System is a centralized platform that enables hostel managers to digitize student onboarding, room allocation, payment tracking, announcements, and issue management.

The system consists of two primary user groups:
- Hostel Administrators (Managers and Porters)
- Students/Tenants

The backend exposes REST APIs consumed by a web application and manages all business operations, data storage, notifications, and integrations.

## 2. System Objectives

The backend should provide:
- Student registration and profile management
- Hostel-specific student management
- Room allocation and roommate pairing
- Payment recording and verification
- Complaint/issue management
- Announcement management
- Notification delivery
- Occupancy tracking
- Annual renewal management

## 3. High-Level Architecture

```
                  Web Application
                        |
              REST API Layer (Spring MVC)
  ----------------------------------------------------
  |                Spring Boot API                    |
  ----------------------------------------------------
  Authentication Service
  Hostel Onboarding Service
  Student Service
  Hostel Service
  Room Service
  Allocation Service
  Payment Service
  Complaint Service
  Announcement Service
  Notification Service
                        |
                  PostgreSQL Database
                        |
  ----------------------------------------------------
  |                External Services                  |
  ----------------------------------------------------
  Paystack
  SMS Provider
  Email Provider (Optional)
```

The backend follows a modular, layered structure within a single Spring Boot application, organized into feature packages (controller, service, repository, entity, dto) rather than a single monolithic file set, with a clear separation between controllers, services, repositories, and entity/data access layers.

## 4. Technology Stack

**Backend Framework**
- Java (JDK 17+)
- Spring Boot (Spring Web / Spring MVC for REST APIs)

**Database**
- PostgreSQL

**Authentication**
- Spring Security
- JWT Authentication (via `jjwt` or Spring Security's OAuth2 Resource Server support)
- `BCryptPasswordEncoder` (built into Spring Security) for password hashing

**ORM / Data Access Layer**
- Spring Data JPA with Hibernate
- PostgreSQL JDBC Driver

**File Storage**
- Cloudinary or AWS S3 (via Spring's `MultipartFile` for upload handling, with the Cloudinary Java SDK or AWS SDK for Java)

**Payment Integration**
- Paystack (consumed via Spring's `RestTemplate` or `WebClient`)

**Notifications**
- SMS Provider (e.g., Twilio Java SDK or a local SMS gateway)
- Email Provider via Spring Boot Starter Mail (`JavaMailSender`) (Optional)

**Documentation**
- Swagger/OpenAPI via `springdoc-openapi`

**Validation & Middleware**
- Jakarta Bean Validation (`@Valid`, Hibernate Validator) for request validation
- Spring Security headers configuration (in place of Helmet) for HTTP security headers
- Spring's built-in CORS configuration (`WebMvcConfigurer` / `@CrossOrigin`) for cross-origin access control

**Process Management**
- Embedded Tomcat (default with Spring Boot) packaged as an executable JAR
- Docker for containerized deployment and process management in production

## 5. User Roles

### Student
Responsibilities:
- Create account
- Complete profile
- Upload student ID
- Upload profile photo
- View room assignment
- View roommate
- Receive announcements
- Submit complaints

### Porter
Responsibilities:
- Verify payments
- Add students manually
- Manage complaints
- Manage room assignments

### Hostel Manager
Responsibilities:
- Manage day-to-day hostel information (the hostel record itself is created by a System Administrator during onboarding)
- Manage rooms
- View student records
- Publish announcements
- Approve allocations
- Monitor occupancy

### System Administrator
Responsibilities:
- Manage hostels
- Manage subscriptions
- Monitor platform health
- Review and respond to hostel onboarding inquiries
- Add new hostels to the system and configure hostel-specific custom student fields
- Create initial Hostel Manager and Porter accounts for newly onboarded hostels

## 6. Core Modules

### Hostel Onboarding & Inquiry Module
Handles:
- Inbound inquiry capture from prospective hostels via a public onboarding page
- Internal tracking of outbound, team-initiated onboarding conversations
- Requirements gathering for hostel-specific customization (custom student fields, pricing, policies)
- Conversion of an inquiry into a fully configured, active hostel

Inquiry Statuses:
- New
- Contacted
- In Discussion
- Onboarded
- Rejected

Access: managed exclusively by System Administrators. Hostel Managers and Porters are not given accounts until after their hostel has been onboarded by an administrator.

### Authentication Module
Handles:
- Login
- Registration
- Password reset
- JWT generation (issued and validated via Spring Security filters)
- Role authorization (via Spring Security method-level/role-based access control, e.g. `@PreAuthorize`)

### Student Module
Stores (base fields, common to every hostel):
- Name
- Student ID Number
- Course
- Level
- Gender
- Phone Number
- Profile Photo
- Student ID Image

Plus any hostel-specific custom fields configured by the administrator for that hostel (e.g., parent/guardian name, parent/guardian phone number, nationality, home address, emergency contact) — see Hostel Module → Custom Student Fields below.

Functions:
- Create profile
- Update profile
- Retrieve student details

### Hostel Module
Stores:
- Hostel Name
- Address
- Capacity
- Pricing
- Contact Information

Functions:
- Manage hostel details
- Track occupancy
- Configure hostel-specific custom student fields

### Custom Student Fields (Per-Hostel Customization)

Since the system is customized for each hostel, every hostel may want to collect different information from students beyond the base profile — for example parent/guardian name, parent/guardian phone number, nationality, home address, or an emergency contact. Rather than hardcoding these per hostel, the backend supports a configurable custom-field model:

- Base fields are fixed and apply to every hostel (see Student Module).
- Additional fields are defined per hostel by a System Administrator, typically during onboarding once requirements have been discussed with the hostel.
- Each custom field has a name, display label, data type (text, number, date, or single-select), and a required/optional flag.
- The student registration form renders dynamically based on the active hostel's configured fields, so the frontend does not need to change when a new field is added.
- Custom field values are stored against the student's record without requiring a database schema change for each new hostel or field — implemented in the entity layer as a `JSONB` column mapped via Hibernate's JSON type support (e.g., `hibernate-types` or `@JdbcTypeCode(SqlTypes.JSON)` in Hibernate 6+), rather than a relational column-per-field design.

### Room Module
Stores:
- Room Number
- Capacity
- Gender Restriction
- Room Type
- Occupancy Status

Functions:
- Room assignment
- Vacancy tracking

### Allocation Module
Handles:
- Student-to-room assignment
- Roommate matching
- Manual reassignment

Business Rules:
- Same gender only
- Existing roommate requests take priority
- Random pairing for unmatched students

### Payment Module
Handles:
- Online payments
- Manual payment verification
- Payment history
- Renewal payments

Payment Status:
- Pending
- Verified
- Failed
- Refunded

### Complaint Module
Handles:
- Issue reporting
- Status tracking
- Resolution updates

Statuses:
- Pending
- In Progress
- Resolved

Daily reminders are generated for unresolved complaints (via a scheduled job using Spring's `@Scheduled` annotation, backed by Spring's `TaskScheduler`).

### Announcement Module
Handles:
- Hostel announcements
- Renewal reminders
- Emergency notices

Announcements are visible to all students belonging to a hostel.

### Notification Module
Handles:
- Payment confirmations
- Room allocation notifications
- Complaint updates
- Renewal reminders

Channels:
- SMS
- Email
- In-App Notifications

## 7. Hostel Onboarding Flow

Hostels are onboarded by the System Administrator, not by Hostel Managers. The platform is customized per hostel, so onboarding includes a requirements conversation before a hostel becomes active. Two paths feed into the same admin-managed step:

### Scenario A: Inbound Inquiry (Onboarding Page)
```
Prospective Hostel Submits Inquiry (Public Onboarding Page)
        |
Inquiry Stored (Status: New)
        |
Admin Reviews Inquiry
        |
Admin Contacts Hostel (Status: Contacted)
        |
Requirements Discussion incl. Custom Fields (Status: In Discussion)
        |
Admin Creates Hostel Record + Configures Custom Fields
        |
Hostel Manager / Porter Accounts Created
        |
Hostel Active (Status: Onboarded)
```

### Scenario B: Team-Initiated Outreach (Primary Channel at Launch)

In the early stages, most hostels will be approached directly by the team rather than arriving through the public form. The admin can log an inquiry internally to keep tracking consistent, then the flow converges with Scenario A:

```
Admin Identifies Prospective Hostel
        |
Admin Logs Inquiry Internally (Status: Contacted)
        |
Requirements Discussion incl. Custom Fields (Status: In Discussion)
        |
Admin Creates Hostel Record + Configures Custom Fields
        |
Hostel Manager / Porter Accounts Created
        |
Hostel Active (Status: Onboarded)
```

> Note: the public inquiry form exists so the system can scale to inbound demand later, but both paths require the same admin-led requirements conversation and configuration step before a hostel goes live.

## 8. Student Onboarding Flow

### Scenario 1: Online Booking
```
Student Registration
        |
Profile Completion
        |
Payment via Paystack
        |
Payment Verification
        |
Student Added To Hostel
        |
Room Allocation
        |
Notification Sent
```

### Scenario 2: Physical Hostel Payment
```
Student Visits Hostel
        |
Pays Through Bank
        |
Receipt Verification
        |
Student Creates Profile
        |
Porter Links Student To Hostel
        |
Room Allocation
        |
Notification Sent
```

## 9. Room Allocation Flow

```
Student Verified
        v
Requested Roommate Exists? (Yes/No)
        v
Yes -> Assign Together   |   No -> Gender Matching -> Random Pairing
        v
Room Available?
        v
Allocate Room
        v
Notification Sent
```

## 10. Complaint Management Flow

```
Student Reports Issue
        v
Stored In Database
        v
Porter Notified
        v
Manager Notified
        v
Issue Assigned
        v
Status Updated
        v
Student Notified
```

## 11. Database Design

> Entities are modeled as JPA `@Entity` classes mapped via Spring Data JPA / Hibernate. Tables below reflect the underlying PostgreSQL schema generated from those entities.

### `users`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| username | String |
| email | String, unique |
| password | String (hashed) |
| role | Enum: student, porter, manager, admin |

### `students`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| phone_number | String |
| user_id | FK -> users.id |
| student_id_number | String |
| course | String |
| level | String |
| gender | Enum: male, female |
| profile_photo_url | String |
| id_card_url | String |
| custom_data | JSONB (hostel-specific custom field values) |

### `hostels`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| phone_number | String |
| name | String |
| address | String |
| capacity | Integer |

### `hostel_custom_fields`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| hostel_id | FK -> hostels.id |
| field_name | String (internal key) |
| field_label | String (display label) |
| field_type | Enum: text, number, date, select |
| is_required | Boolean |
| options | JSONB, nullable (choices for select type) |

### `hostel_inquiries`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| hostel_name | String |
| contact_person | String |
| phone | String |
| email | String |
| location | String |
| notes | Text |
| status | Enum: new, contacted, in_discussion, onboarded, rejected |
| created_at | Timestamp |

### `rooms`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| hostel_id | FK -> hostels.id |
| room_number | String |
| room_type | String |
| capacity | Integer |

### `room_allocations`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| student_id | FK -> students.id |
| room_id | FK -> rooms.id |
| roommate_id | FK -> students.id, nullable |

### `payments`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| student_id | FK -> students.id |
| amount | Decimal |
| payment_method | String |
| status | Enum: pending, verified, failed, refunded |
| reference | String, unique |

### `complaints`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| student_id | FK -> students.id |
| title | String |
| description | Text |
| status | Enum: pending, in_progress, resolved |

### `announcements`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| hostel_id | FK -> hostels.id |
| title | String |
| message | Text |

### `notifications`
| Field | Type |
|---|---|
| id | UUID / Serial PK |
| recipient_id | FK -> users.id |
| type | String |
| status | Enum: sent, pending, failed |

## 12. Security

Measures:
- JWT Authentication (Spring Security)
- Password Hashing (`BCryptPasswordEncoder`)
- Role-Based Access Control (Spring Security `@PreAuthorize` / role-based filter chains per route)
- Input Validation (Jakarta Bean Validation annotations on request DTOs, e.g. `@NotBlank`, `@Email`, `@Size`)
- Audit Logging
- Spring Security HTTP security headers configuration (equivalent to Helmet)
- Rate limiting on authentication and public endpoints

## 13. Additional Architecture Considerations

The following areas extend the original design and are recommended for a production-grade Spring Boot backend.

**API Rate Limiting**
- Apply `Bucket4j` (or a Redis-backed limiter, e.g. `bucket4j-redis`) on authentication, payment, and public-facing endpoints to mitigate brute-force and abuse.
- Use stricter limits on login/password-reset routes than on general read endpoints.

**Caching Strategy**
- Use Spring's caching abstraction (`@Cacheable`, `@CacheEvict`) backed by Redis for frequently read, rarely changing data (e.g., hostel listings, room availability, announcements).
- Invalidate or update cache entries on writes to allocation, room, and announcement records.
- Consider HTTP caching headers (ETag/Cache-Control) for public GET endpoints, configurable via Spring MVC's `ShallowEtagHeaderFilter`.

**Error Handling Standards**
- Centralized exception handling via `@RestControllerAdvice` / `@ExceptionHandler` that returns a consistent JSON error shape (code, message, details).
- Custom exception classes (e.g., `NotFoundException`, `ValidationException`, `UnauthorizedException`) mapped to appropriate HTTP status codes via `@ResponseStatus` or the global exception handler.
- Standard Spring MVC exception propagation handles asynchronous and synchronous request failures without manual promise/callback wrapping.

**Logging & Monitoring**
- Structured logging with SLF4J + Logback (Spring Boot's default logging stack), including request/trace IDs for traceability (e.g., via Spring Cloud Sleuth or Micrometer Tracing).
- Centralized log aggregation (e.g., ELK stack or a hosted log service).
- Application performance monitoring and uptime checks via Spring Boot Actuator combined with Micrometer, exported to Prometheus/Grafana or a hosted APM tool.
- Health-check endpoint (`/actuator/health`, provided out of the box by Spring Boot Actuator) for load balancers and uptime monitors.

**Environment Configuration**
- Configuration managed via `application.yml`/`application.properties` profiles (`application-dev.yml`, `application-staging.yml`, `application-prod.yml`), with environment variable overrides and a secrets manager in production.
- Separate configuration profiles for development, staging, and production using Spring's `@Profile` / `spring.profiles.active`.
- No secrets (API keys, DB credentials, JWT secret) committed to source control.

**API Versioning**
- Prefix routes with a version segment (e.g., `/api/v1/...`) to allow non-breaking evolution of the API.
- Deprecation policy documented for any breaking changes to existing versions.

**Deployment & CI/CD**
- Containerization with Docker for consistent runtime environments across development, staging, and production, packaging the Spring Boot application as an executable JAR inside a slim JDK base image.
- CI/CD pipeline (e.g., GitHub Actions) to run linting, tests (via Maven/Gradle), and automated deployment on merge.
- Container orchestration (e.g., Docker Compose, or Kubernetes at larger scale) for process management and zero-downtime restarts, replacing PM2 (which is Node-specific).

**Testing Strategy**
- Unit tests for services and business logic (JUnit 5 + Mockito).
- Integration tests for REST endpoints (Spring Boot Test with `MockMvc` or `RestAssured`).
- Test database isolated from development/production data (e.g., via Testcontainers running a real PostgreSQL instance, or an in-memory H2 database for lightweight tests).

**Database Migrations**
- Schema migrations managed through Flyway or Liquibase (in place of Prisma Migrate/Sequelize CLI) rather than manual schema edits, with Hibernate's `ddl-auto` left disabled or set to `validate` in production.
- Seed scripts for reproducible local/test environments.

**API Documentation**
- Swagger/OpenAPI specification generated and kept in sync with route definitions via `springdoc-openapi`, served at a `/swagger-ui.html` (or `/docs`) endpoint for internal and partner reference.

## 14. Future Enhancements

- Hostel discovery marketplace
- Hostel reviews
- Online booking system
- AI-powered roommate matching
- Mobile application
- Push notifications
- Analytics dashboard

## 15. Conclusion

The backend follows a modular, layered architecture using Java, Spring Boot, Spring Data JPA, and PostgreSQL. The design prioritizes simplicity, maintainability, and rapid development while supporting future expansion into a complete hostel marketplace and tenant management ecosystem.
