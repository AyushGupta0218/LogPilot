package com.logpilot.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class LogRepository {
 private final JdbcTemplate jdbc; private final ObjectMapper mapper;
 public LogRepository(JdbcTemplate jdbc,ObjectMapper mapper){this.jdbc=jdbc;this.mapper=mapper;}
 public int save(LogEvent e){try{return jdbc.update("INSERT INTO log_events(event_id,service,level,event_timestamp,message,trace_id,metadata) VALUES (?,?,?,?,?,?,?::jsonb) ON CONFLICT(event_id) DO NOTHING",e.eventId(),e.service(),e.level(),Timestamp.from(e.timestamp()),e.message(),e.traceId(),mapper.writeValueAsString(e.metadata()));}catch(JsonProcessingException ex){throw new IllegalArgumentException("Metadata must be JSON serializable",ex);}}
 public List<LogEvent> search(String service,String level,String q,int limit){return jdbc.query("SELECT event_id,service,level,event_timestamp,message,trace_id,metadata::text FROM log_events WHERE (CAST(? AS text) IS NULL OR service=?) AND (CAST(? AS text) IS NULL OR level=?) AND (CAST(? AS text) IS NULL OR message ILIKE '%' || ? || '%') ORDER BY event_timestamp DESC LIMIT ?",ROW,service,service,level,level,q,q,limit);}
 public List<LogEvent> recentForAnalysis(String service,Instant from,Instant to,int limit){return jdbc.query("SELECT event_id,service,level,event_timestamp,message,trace_id,metadata::text FROM log_events WHERE (CAST(? AS text) IS NULL OR service=?) AND (CAST(? AS timestamptz) IS NULL OR event_timestamp>=?) AND (CAST(? AS timestamptz) IS NULL OR event_timestamp<=?) ORDER BY event_timestamp DESC LIMIT ?",ROW,service,service,from,from,to,to,limit);}
 private final RowMapper<LogEvent> ROW=(rs,n)->new LogEvent((UUID)rs.getObject("event_id"),rs.getString("service"),rs.getString("level"),rs.getTimestamp("event_timestamp").toInstant(),rs.getString("message"),rs.getString("trace_id"),readMetadata(rs.getString("metadata")));
 private Map<String,Object> readMetadata(String json){try{return mapper.readValue(json,mapper.getTypeFactory().constructMapType(Map.class,String.class,Object.class));}catch(Exception e){return Map.of();}}
}
