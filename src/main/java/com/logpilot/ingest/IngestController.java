package com.logpilot.ingest;

import com.logpilot.logs.LogEvent;
import com.logpilot.logs.LogRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/logs")
public class IngestController { private final LogRepository repository; public IngestController(LogRepository repository){this.repository=repository;}
 @PostMapping("/ingest") public Map<String,Integer> ingest(@Valid @RequestBody IngestRequest request){int accepted=0;for(var e:request.events()){if(e.service()==null||e.service().isBlank()||e.level()==null||e.timestamp()==null||e.message()==null||e.message().isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"service, level, timestamp, and message are required");String level=e.level().toUpperCase();if(!Set.of("TRACE","DEBUG","INFO","WARN","ERROR").contains(level))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unsupported log level: "+level);accepted+=repository.save(new LogEvent(e.eventId()==null?UUID.randomUUID():e.eventId(),e.service(),level,e.timestamp(),e.message(),e.traceId(),e.metadata()==null?Map.of():e.metadata()));}return Map.of("accepted",accepted,"received",request.events().size());}
}
