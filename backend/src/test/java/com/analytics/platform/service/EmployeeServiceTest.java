package com.analytics.platform.service;

import com.analytics.platform.dto.request.EmployeeRequest;
import com.analytics.platform.dto.response.EmployeeResponse;
import com.analytics.platform.entity.Department;
import com.analytics.platform.entity.Employee;
import com.analytics.platform.exception.DuplicateResourceException;
import com.analytics.platform.exception.ResourceNotFoundException;
import com.analytics.platform.repository.DepartmentRepository;
import com.analytics.platform.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;
    private Department testDepartment;
    private EmployeeRequest employeeRequest;
    private UUID employeeId;
    private UUID departmentId;

    @BeforeEach
    void setUp() {
        employeeId = UUID.randomUUID();
        departmentId = UUID.randomUUID();

        testDepartment = new Department();
        testDepartment.setId(departmentId);
        testDepartment.setName("Operations");
        testDepartment.setActive(true);

        testEmployee = new Employee();
        testEmployee.setId(employeeId);
        testEmployee.setEmployeeCode("EMP001");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setDepartment(testDepartment);
        testEmployee.setJobTitle("Analyst");
        testEmployee.setLocation("New York");
        testEmployee.setShift("DAY");
        testEmployee.setStatus("ACTIVE");

        employeeRequest = new EmployeeRequest();
        employeeRequest.setEmployeeCode("EMP001");
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john.doe@example.com");
        employeeRequest.setDepartmentId(departmentId.toString());
        employeeRequest.setJobTitle("Analyst");
        employeeRequest.setLocation("New York");
        employeeRequest.setShift("DAY");
        employeeRequest.setStatus("ACTIVE");
    }

    @Test
    void getAllEmployees_Success() {
        Page<Employee> page = new PageImpl<>(java.util.List.of(testEmployee));
        when(employeeRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<EmployeeResponse> result = employeeService.getAllEmployees(0, 20, "lastName,asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(employeeRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));

        EmployeeResponse result = employeeService.getEmployeeById(employeeId.toString());

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(employeeRepository).findById(employeeId);
    }

    @Test
    void getEmployeeById_NotFound_ThrowsException() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(employeeId.toString()));
    }

    @Test
    void createEmployee_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(employeeRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeResponse result = employeeService.createEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(departmentRepository).findById(departmentId);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicateCode_ThrowsException() {
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.existsByEmployeeCode("EMP001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(employeeRequest));
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(testDepartment));
        when(employeeRepository.existsByEmployeeCodeAndIdNot("EMP001", employeeId)).thenReturn(false);
        when(employeeRepository.existsByEmailAndIdNot("john.doe@example.com", employeeId)).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeResponse result = employeeService.updateEmployee(employeeId.toString(), employeeRequest);

        assertNotNull(result);
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeRepository).delete(testEmployee);

        employeeService.deleteEmployee(employeeId.toString());

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(testEmployee);
    }
}
