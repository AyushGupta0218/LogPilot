package com.logpilot.web;

import com.logpilot.logs.LogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/logs")
public class LogQueryController {private final LogRepository repository;public LogQueryController(LogRepository repository){this.repository=repository;}@GetMapping public List<?> search(@RequestParam(required=false) String service,@RequestParam(required=false) String level,@RequestParam(required=false) String q,@RequestParam(defaultValue="100") int limit){if(limit<1||limit>500)throw new IllegalArgumentException("limit must be between 1 and 500");return repository.search(service,level==null?null:level.toUpperCase(),q,limit);}}
