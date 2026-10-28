package com.logpilot.ingest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record IngestRequest(@NotEmpty @Size(max=500) List<@Valid Event> events) { public record Event(UUID eventId, @Size(max=100) String service, String level, Instant timestamp, @Size(max=4000) String message, @Size(max=200) String traceId, Map<String,Object> metadata) {} }
