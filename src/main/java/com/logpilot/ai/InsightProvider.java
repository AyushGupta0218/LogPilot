package com.logpilot.ai;
import com.logpilot.logs.LogEvent;import java.util.List;
public interface InsightProvider { InsightResult analyze(List<LogEvent> events); String name(); }
