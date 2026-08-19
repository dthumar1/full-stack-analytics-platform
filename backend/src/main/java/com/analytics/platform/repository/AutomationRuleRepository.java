package com.analytics.platform.repository;

import com.analytics.platform.entity.AutomationRule;
import com.analytics.platform.entity.AutomationRule.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationRuleRepository extends JpaRepository<AutomationRule, String> {
    
    List<AutomationRule> findByEnabledTrue();
    List<AutomationRule> findByRuleType(RuleType ruleType);
    List<AutomationRule> findByEnabledTrueAndRuleType(RuleType ruleType);
}
