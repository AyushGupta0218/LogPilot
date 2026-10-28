package com.logpilot.logs;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record LogEvent(UUID eventId, String service, String level, Instant timestamp, String message, String traceId, Map<String,Object> metadata) {}
