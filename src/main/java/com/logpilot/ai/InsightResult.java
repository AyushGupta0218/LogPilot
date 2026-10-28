package com.logpilot.ai;
import java.util.List;
public record InsightResult(String summary,List<String> possibleCauses,List<String> recommendedChecks,List<String> evidenceEventIds,String provider) {}
