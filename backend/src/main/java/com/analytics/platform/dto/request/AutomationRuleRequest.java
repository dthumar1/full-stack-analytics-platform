package com.analytics.platform.dto.request;

import com.analytics.platform.entity.AutomationRule.RuleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRuleRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Rule type is required")
    private RuleType ruleType;
    
    private BigDecimal threshold;
    
    private Boolean enabled;
}
