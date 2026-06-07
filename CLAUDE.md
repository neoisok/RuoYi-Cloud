# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RuoYi-Cloud v3.6.5 — a full-stack, microservices-based rapid development platform. The base platform (RuoYi) provides admin/management features. A custom **HRP (Hospital Resource Planning)** module has been added on top for hospital cost/revenue/employee management.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend framework | Spring Boot 2.7.18, Spring Cloud 2021.0.9, Spring Cloud Alibaba 2021.0.6.1 |
| Service discovery & config | Nacos (standalone mode, port 8848) |
| API gateway | Spring Cloud Gateway (port 8080) |
| Auth | Spring Security + JWT (auth service port 9200) |
| Database | MySQL 5.7 with MyBatis + PageHelper for pagination |
| Cache | Redis (used for auth tokens and session) |
| Flow control | Sentinel |
| Distributed transactions | Seata |
| Frontend | Vue 2.6, Element UI 2.15, Vue CLI 4, Vuex, Vue Router |
| Build | Maven (Java 8 source/target), npm |

## Build & Run Commands

### Backend (Maven)

```bash
# Build all modules (skip tests)
mvn clean package -Dmaven.test.skip=true

# Build a specific module
mvn clean package -Dmaven.test.skip=true -pl ruoyi-modules/ruoyi-system
```

### Frontend (ruoyi-ui/)

```bash
cd ruoyi-ui

# Dev server (port 80, proxies /dev-api to localhost:8080 gateway)
npm run dev

# Production build
npm run build:prod

# Staging build
npm run build:stage

# Lint
npm run lint
```

### Run individual services

Each service is a Spring Boot app. Run `*Application.java` directly in your IDE, or use the `.bat` scripts in `bin/` (Windows only). Service ports:
- Gateway: 8080
- Auth: 9200
- System module: 9201
- Code gen: 9202
- Job scheduler: 9203
- File service: 9300
- Monitor (Spring Boot Admin): 9100

### Docker

```bash
cd docker
docker-compose up -d   # starts Nacos, MySQL, Redis, Nginx, and all services
```

## Architecture

### Request Flow

```
Browser (ruoyi-ui port 80) → Gateway (8080) → Auth (9200) / System (9201) / ...
                                  ↓
                             Nacos (8848) — service registry & config
                             Redis — token cache
```

The Vue dev server proxies `/dev-api` requests to `localhost:8080` (the gateway). The gateway routes by service name registered in Nacos.

### Module Map

```
com.ruoyi (parent pom)
├── ruoyi-gateway        — API gateway, request routing, rate limiting (Sentinel), captcha
├── ruoyi-auth           — Login, token issuance, logout (Spring Security + JWT)
├── ruoyi-api            — Feign remote service interfaces (only ruoyi-api-system currently)
├── ruoyi-common         — Shared libraries:
│   ├── ruoyi-common-core        — Base entity, utils, exception handling, web controllers
│   ├── ruoyi-common-security    — Auth annotations, JWT utils, permission aspect
│   ├── ruoyi-common-redis       — Redis config and cache utils
│   ├── ruoyi-common-datasource  — Dynamic multi-datasource support
│   ├── ruoyi-common-datascope   — Row-level data permission filtering
│   ├── ruoyi-common-log         — Operation/error log recording
│   ├── ruoyi-common-sensitive   — Field-level data masking
│   ├── ruoyi-common-swagger     — Springdoc/OpenAPI config
│   └── ruoyi-common-seata       — Seata distributed transaction config
├── ruoyi-modules        — Business services:
│   ├── ruoyi-system     — User/role/menu/dept/dict management (core admin) + HRP logic
│   ├── ruoyi-gen        — Code generator (Velocity templates)
│   ├── ruoyi-job        — Quartz-based scheduled task management
│   └── ruoyi-file       — File upload/storage (FastDFS or MinIO)
├── ruoyi-visual         — Monitoring:
│   └── ruoyi-visual-monitor — Spring Boot Admin dashboard (port 9100)
└── ruoyi-ui             — Vue 2 frontend (Element UI admin panel)
```

### Backend Layered Architecture (per module)

Each business module follows a standard Spring MVC + MyBatis pattern:

```
controller/       — REST controllers, @RestController annotated
  domain/         — Entity/DTO classes (POJOs)
  mapper/         — MyBatis mapper interfaces
  service/        — Service interfaces
    impl/         — Service implementations
resources/
  bootstrap.yml   — App name, Nacos config, active profile
  mapper/         — MyBatis XML mapper files (one per entity)
```

### API Module Pattern

`ruoyi-api/ruoyi-api-system` defines Feign client interfaces for cross-service calls. Other modules depend on this API module and implement or consume these interfaces. Custom HRP API definitions are under `com.hrp.cost.api`.

### Frontend Architecture (ruoyi-ui)

```
src/
├── api/           — API request modules (axios calls to backend)
│   ├── system/    — System management APIs (user, role, menu, dept, etc.)
│   ├── hrp/       — HRP custom APIs (costItem, revenueItem, employee, campus, dept, informationItem)
│   └── ...
├── views/         — Page components
│   ├── system/    — System admin pages
│   ├── hrp/       — HRP custom pages
│   │   ├── basicinfo/  — cost, revenue, information, campus, dept, employee, allocationRule
│   │   └── employee/   — employee management
│   ├── monitor/   — Monitor pages
│   └── tool/      — Tool pages (code gen)
├── store/         — Vuex state management (modules/)
├── router/        — Vue Router config
├── components/    — Shared components
└── utils/         — Utility functions (request.js for axios interceptors)
```

The `@` alias resolves to `src/`. API calls use the `VUE_APP_BASE_API` env var (`/dev-api` in dev) which is proxied to the gateway.

## HRP Custom Module

HRP (Hospital Resource Planning) is custom code added to the standard RuoYi platform:

- **Backend**: Java classes under `com.hrp.cost` in `ruoyi-modules/ruoyi-system` (controller, service, mapper, domain). API definitions under `com.hrp.cost.api` in `ruoyi-api/ruoyi-api-system`.
- **Frontend**: Pages under `ruoyi-ui/src/views/hrp/`, API modules under `ruoyi-ui/src/api/hrp/`.
- **Entities**: CostItem (成本项目), RevenueItem (收入项目), InformationItem (信息项目), Campus (院区), HospitalDept (医院科室), HrpEmployee (人员档案)
- Each entity follows the same CRUD pattern as the platform's system entities (extends BaseEntity, uses PageHelper for pagination, MyBatis XML for SQL).

## Configuration

- **Nacos** (127.0.0.1:8848) is the centralized config source. Each service's `bootstrap.yml` specifies the Nacos server address and shared configs.
- **Profiles**: `dev` (default), can be overridden via `spring.profiles.active`.
- **Database**: `ry-cloud` database, with SQL scripts in `sql/` directory.
- Frontend env vars are in `.env.*` files in `ruoyi-ui/`.

## Key Dependencies

- **PageHelper** (`com.github.pagehelper`) — wraps all MyBatis queries with automatic pagination via `startPage()`
- **FastJSON2** — JSON serialization throughout the platform
- **Springdoc OpenAPI** — API documentation (webflux variant for gateway, webmvc variant for other services)
- **Kaptcha** — captcha generation
- **Druid** — database connection pool
- **FastDFS / MinIO** — file storage (configurable)

## Database

SQL scripts are in `sql/`:
- `ry_20240629.sql` — main schema + seed data
- `ry_config_20250224.sql` — Nacos config data
- `quartz.sql` — Quartz scheduler tables
- `ry_seata_20210128.sql` — Seata transaction tables
