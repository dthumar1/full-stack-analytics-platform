package com.analytics.platform.scheduler;

import com.analytics.platform.entity.AutomationRun.AutomationType;
import com.analytics.platform.service.AutomationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutomationScheduler {
    
    private final AutomationService automationService;
    
    @Value("${automation.enabled:true}")
    private boolean automationEnabled;
    
    @Scheduled(cron = "${automation.cron-expression:0 0 */6 * * *}")
    public void runScheduledAutomations() {
        if (!automationEnabled) {
            log.info("Automation is disabled, skipping scheduled run");
            return;
        }
        
        log.info("Starting scheduled automation run");
        
        try {
            // Run productivity check
            automationService.runAutomation(AutomationType.PRODUCTIVITY_CHECK);
            
            // Run error rate check
            automationService.runAutomation(AutomationType.ERROR_RATE_CHECK);
            
            // Run deadline check
            automationService.runAutomation(AutomationType.DEADLINE_CHECK);
            
            log.info("Completed scheduled automation run");
            
        } catch (Exception ex) {
            log.error("Error during scheduled automation run", ex);
        }
    }
}
