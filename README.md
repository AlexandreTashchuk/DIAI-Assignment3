# Nova Events (Assignments 3, 4, 5)

SSR CRUD application with Spring Boot, Thymeleaf, JPA/MySQL, and JWT-based security.

## Prerequisites

- Java 17
- Maven Wrapper (`mvnw.cmd` is included)
- Docker Desktop (daemon running)
- IntelliJ IDEA (recommended for running the Spring app)

## Local Database (Docker)

From the project root:

```cmd
docker compose up -d mysql
docker compose ps
```

To stop the DB:

```cmd
docker compose stop mysql
```

To fully reset DB data (destructive):

```cmd
docker compose down -v
docker compose up -d mysql
```

## Run the App

### Option A - IntelliJ

1. Open project in IntelliJ.
2. Ensure Docker MySQL container is running.
3. Run `NovaeventsApplicationKt`.

### Option B - Terminal

```cmd
"%CD%\mvnw.cmd" spring-boot:run
```

App default URL: `http://localhost:8080`

## Default Seeded Users

- `alice` / `password123` -> `ROLE_EDITOR`
- `bob` / `password123` -> `ROLE_EDITOR`
- `charlie` / `password123` -> `ROLE_ADMIN`

## Manual Smoke Test

1. Open `http://localhost:8080/login`.
2. Confirm page contains `Nova Events Sign In`.
3. Login with `alice` / `password123`.
4. Confirm navbar shows:
   - username in element with id `navbar-username`
   - `Logout` button
5. Logout and confirm navbar username/logout controls disappear for anonymous users.

## Why the Startup FK Error Happened

When ownership (`event.owner_id`) was introduced, legacy rows in `event` had `owner_id = 0`.
Hibernate then tried to add a foreign key `event.owner_id -> app_users.id`, which failed because user id `0` does not exist.

### Fix Applied

- Kept `Event.owner` join column nullable during migration.
- Added startup backfill in `DataInitializer` (`backfillMissingOwners`) to repair legacy rows.
- Seeded startup events with real owners distributed across `alice`, `bob`, and `charlie`.

This prevents FK creation failures on upgraded databases while keeping new events owned correctly.

## Security Notes

- Authentication uses form login + JWT in HttpOnly `jwt` cookie.
- App is stateless (`SessionCreationPolicy.STATELESS`).
- CSRF protection is enabled.
- Request logging interceptor logs requests as:
  - `[principal] METHOD URI [status]`
