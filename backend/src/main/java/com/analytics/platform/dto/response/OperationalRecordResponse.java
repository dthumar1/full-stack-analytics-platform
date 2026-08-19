package com.analytics.platform.dto.response;

import com.analytics.platform.entity.OperationalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalRecordResponse {
    
    private String id;
    private String employeeId;
    private String employeeName;
    private String departmentId;
    private String departmentName;
    private String processType;
    private Integer unitsProcessed;
    private BigDecimal hoursWorked;
    private BigDecimal productivityRate;
    private Integer errors;
    private BigDecimal errorRate;
    private String status;
    private String location;
    private LocalDate workDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static OperationalRecordResponse from(OperationalRecord record) {
        return OperationalRecordResponse.builder()
            .id(record.getId())
            .employeeId(record.getEmployee() != null ? record.getEmployee().getId() : null)
            .employeeName(record.getEmployee() != null ? 
                record.getEmployee().getFirstName() + " " + record.getEmployee().getLastName() : null)
            .departmentId(record.getDepartment() != null ? record.getDepartment().getId() : null)
            .departmentName(record.getDepartment() != null ? record.getDepartment().getName() : null)
            .processType(record.getProcessType() != null ? record.getProcessType().name() : null)
            .unitsProcessed(record.getUnitsProcessed())
            .hoursWorked(record.getHoursWorked())
            .productivityRate(record.getProductivityRate())
            .errors(record.getErrors())
            .errorRate(record.getErrorRate())
            .status(record.getStatus() != null ? record.getStatus().name() : null)
            .location(record.getLocation())
            .workDate(record.getWorkDate())
            .createdAt(record.getCreatedAt())
            .updatedAt(record.getUpdatedAt())
            .build();
    }
}
