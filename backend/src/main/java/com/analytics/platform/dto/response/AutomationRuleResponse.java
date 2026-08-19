package com.analytics.platform.dto.response;

import com.analytics.platform.entity.AutomationRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRuleResponse {
    
    private String id;
    private String name;
    private String description;
    private String ruleType;
    private BigDecimal threshold;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static AutomationRuleResponse from(AutomationRule rule) {
        return AutomationRuleResponse.builder()
            .id(rule.getId())
            .name(rule.getName())
            .description(rule.getDescription())
            .ruleType(rule.getRuleType() != null ? rule.getRuleType().name() : null)
            .threshold(rule.getThreshold())
            .enabled(rule.getEnabled())
            .createdAt(rule.getCreatedAt())
            .updatedAt(rule.getUpdatedAt())
            .build();
    }
}
