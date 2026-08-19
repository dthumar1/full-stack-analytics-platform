package com.analytics.platform.dto.request;

import com.analytics.platform.entity.OperationalRecord.ProcessType;
import com.analytics.platform.entity.OperationalRecord.RecordStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalRecordRequest {
    
    @NotNull(message = "Employee ID is required")
    private String employeeId;
    
    @NotNull(message = "Department ID is required")
    private String departmentId;
    
    @NotNull(message = "Process type is required")
    private ProcessType processType;
    
    @NotNull(message = "Units processed is required")
    @Positive(message = "Units processed must be positive")
    private Integer unitsProcessed;
    
    @NotNull(message = "Hours worked is required")
    @Positive(message = "Hours worked must be positive")
    private String hoursWorked;
    
    @NotNull(message = "Errors is required")
    @PositiveOrZero(message = "Errors must be zero or positive")
    private Integer errors;
    
    private RecordStatus status;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Work date is required")
    private LocalDate workDate;
}
