# Portfolio SaaS — Agent Guidelines

## What This Project Is

A portfolio/resume SaaS platform. Users register, create profiles (projects, experiences, books), and share public portfolios via `/u/{username}`.

**Two distinct frontends:**
- **Static landing page** (`index.html`, `script.js`, `styles.css` at root) — marketing page, pure HTML/CSS/JS, no build step
- **Thymeleaf templates** (`backend/src/main/resources/templates/`) — server-side rendered dashboard, profile editor, public profiles

---

## Architecture

```
Root
├── index.html / script.js / styles.css   ← Static landing page (GitHub Pages-deployable)
└── backend/                              ← Spring Boot 3.4.4, Java 21
    ├── src/main/java/com/portfoliosass/
    │   ├── config/SecurityConfig.java    ← Route protection rules
    │   ├── controller/                   ← MVC + REST endpoints
    │   ├── model/                        ← User, Profile, Project, Experience, Book
    │   ├── security/                     ← JWT filter + UserDetailsServiceImpl
    │   └── service/                      ← UserService, ProfileService
    ├── src/main/resources/
    │   ├── templates/                    ← Thymeleaf HTML templates
    │   └── application.yml               ← Config (uses env vars: DB_USERNAME, DB_PASSWORD, JWT_SECRET)
    └── docker-compose.yml                ← PostgreSQL 16 for local dev
```

**Data model:** `User` 1→1 `Profile` 1→many `Project`, `Experience`, `Book`

**Auth:** JWT stored in `HttpOnly` cookie, 24h expiration, `STATELESS` session. CSRF disabled.

---

## Build & Run

```bash
# 1. Start the database
cd backend && docker-compose up -d

# 2. Run the backend (dev profile)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
# App available at http://localhost:8080

# 3. Build JAR for production
mvn clean package -DskipTests

# 4. Run tests
mvn test
```

The static landing page requires no build step — open `index.html` in a browser directly.

---

## Key Routes

| Path | Access | Handler |
|------|--------|---------|
| `/` | Public | Landing page (index.html in templates) |
| `/u/{username}` | Public | Public portfolio |
| `/login`, `/register` | Public | Auth forms |
| `/dashboard`, `/profile` | Authenticated | User area |
| `/admin/**` | `ROLE_ADMIN` | Admin panel |
| `/api/auth/login` | Public | REST JSON login → returns JWT |

---

## Environment Variables (Production)

| Variable | Default | Notes |
|----------|---------|-------|
| `DB_USERNAME` | `postgres` | PostgreSQL user |
| `DB_PASSWORD` | `postgres` | PostgreSQL password |
| `JWT_SECRET` | weak default | **Must override in prod** (min 256-bit key) |

`server.cookie.secure=false` in dev — must be `true` in production (HTTPS).

---

## GitHub Pages Deployment

> **Only the static landing page** (`index.html`, `script.js`, `styles.css`) can be deployed to GitHub Pages. The Spring Boot backend and Thymeleaf templates require a server and cannot run on GitHub Pages.

### Option A — Deploy manually via repository settings

1. Push the repo to GitHub.
2. Go to **Settings → Pages**.
3. Set source to **Deploy from a branch**, select `main`, folder `/` (root).
4. GitHub Pages will serve `index.html` from the root automatically.

### Option B — Automated deploy via GitHub Actions

Use the workflow at `.github/workflows/deploy-pages.yml` (see prompt `/deploy-github-pages` to generate it).

The workflow:
- Triggers on push to `main`
- Copies `index.html`, `script.js`, `styles.css` to the Pages artifact
- Deploys via the `actions/deploy-pages` action

### What works on GitHub Pages

- The marketing landing page (hero, about, skills, projects, pricing, contact form UI)
- Static assets (fonts, CSS, JS)

### What does NOT work on GitHub Pages

- Login / Register (requires Spring Boot)
- Dashboard, Profile editor (requires Spring Boot + DB)
- Public portfolio URLs `/u/{username}` (requires Spring Boot + DB)
- Contact form submission (requires a backend endpoint)

For full functionality, deploy the backend separately (e.g., Railway, Render, Fly.io) and optionally host the static landing page on GitHub Pages with CTA links pointing to the backend URL.

---

## Common Pitfalls

- `application.yml` has `spring.profiles.active: dev` hardcoded — override via `SPRING_PROFILES_ACTIVE=prod` env var in production.
- `jpa.hibernate.ddl-auto: update` is fine for dev; use `validate` in production with proper migrations.
- Thymeleaf cache is `false` by default — set to `true` in production.
- The `init.sql` creates a default admin: username=`admin`, password=`password` — change this before going live.
