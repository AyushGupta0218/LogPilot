CREATE TABLE log_events (
    event_id UUID PRIMARY KEY,
    org_id TEXT NOT NULL DEFAULT 'demo',
    service TEXT NOT NULL,
    level TEXT NOT NULL CHECK (level IN ('TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR')),
    event_timestamp TIMESTAMPTZ NOT NULL,
    message TEXT NOT NULL,
    trace_id TEXT,
    metadata JSONB NOT NULL DEFAULT '{}'::jsonb,
    ingested_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_log_events_org_timestamp ON log_events (org_id, event_timestamp DESC);
CREATE INDEX idx_log_events_org_service_timestamp ON log_events (org_id, service, event_timestamp DESC);
CREATE INDEX idx_log_events_org_level_timestamp ON log_events (org_id, level, event_timestamp DESC);
CREATE INDEX idx_log_events_trace_id ON log_events (trace_id) WHERE trace_id IS NOT NULL;

CREATE TABLE insight_analyses (
    analysis_id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    service_filter TEXT,
    from_timestamp TIMESTAMPTZ,
    to_timestamp TIMESTAMPTZ,
    provider TEXT NOT NULL,
    event_ids JSONB NOT NULL,
    result JSONB NOT NULL
);

CREATE INDEX idx_insight_analyses_created ON insight_analyses (created_at DESC);
