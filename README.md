# LogPilot

A compact log investigation app: ingest structured events, search in PostgreSQL, and produce an AI-assisted incident snapshot from bounded recent events. It is a Java/Spring Boot modular monolith designed as an approachable portfolio project.

## Stack

Java 21, Spring Boot, PostgreSQL with Flyway, Thymeleaf UI, JSON REST API. Docker Compose starts the app and PostgreSQL; Redis is not required. The insight provider is deterministic mock mode by default.

## Run locally

Prerequisite: Docker Compose.

```sh
cp .env.example .env
docker compose up --build
```

Open http://localhost:8080. Health: http://localhost:8080/actuator/health.

## API

Ingest up to 500 events in one request: `POST /api/v1/logs/ingest`

```sh
curl -X POST http://localhost:8080/api/v1/logs/ingest -H 'Content-Type: application/json' -d '{"events":[{"service":"checkout","level":"ERROR","timestamp":"2026-10-28T10:00:00Z","message":"Payment provider timed out","traceId":"trace-123","metadata":{"region":"ap-south-1"}}]}'
```

Search with `GET /api/v1/logs?service=checkout&level=ERROR&limit=50` (`q` optionally searches message text). Analyze with `POST /api/v1/insights/analyze`; request fields `service`, `from`, and `to` are optional.

## AI and data handling

Mock analysis is the default and makes no external model calls. To opt into OpenAI analysis, set `AI_PROVIDER=openai` and `AI_API_KEY` in `.env`; only bounded event messages are sent, with email addresses and common key-value secrets redacted. The request disables response storage and treats log content as untrusted input. Log messages may still contain sensitive data; only enable this when you are allowed to share selected data with the provider. Never commit `.env` or API keys.

## Scope

This is a local portfolio demo, not a multi-tenant production service. Authentication, rate limits, retention, robust pagination, and a real model adapter are future work.
