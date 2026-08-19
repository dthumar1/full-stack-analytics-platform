package com.analytics.platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {
    
    private String metric;
    private String groupBy;
    private List<Map<String, Object>> data;
    private Map<String, Object> summary;
}
