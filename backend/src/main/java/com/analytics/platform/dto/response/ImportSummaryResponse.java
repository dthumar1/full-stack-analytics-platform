package com.analytics.platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportSummaryResponse {
    
    private Integer totalRows;
    private Integer successfulRows;
    private Integer failedRows;
    private List<ImportError> errors;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImportError {
        private Integer rowNumber;
        private String data;
        private String error;
    }
}
