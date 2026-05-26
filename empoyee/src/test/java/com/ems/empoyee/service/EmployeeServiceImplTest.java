package com.ems.empoyee.service;

import com.ems.empoyee.dto.EmployeeDTO;
import com.ems.empoyee.entity.Employee;
import com.ems.empoyee.exception.EmployeeException;
import com.ems.empoyee.repository.EmployeeRepository;
import com.ems.empoyee.serviceimpl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId("EMP-001");
        employee.setName("chammu");
        employee.setAge(28);
        employee.setAddress("123 Maple Street, Springfield, IL");

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId("EMP-001");
        employeeDTO.setName("chammu");
        employeeDTO.setAge(28);
        employeeDTO.setAddress("123 Maple Street, Springfield, IL");
    }

    @Test
    void createEmployee_Success() throws EmployeeException {
        // employee does not exist yet
        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        assertDoesNotThrow(() -> employeeService.createEmployee(employeeDTO));

        verify(employeeRepository, times(1)).findById("EMP-001");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    @Test
    void createEmployee_ThrowsException_WhenEmployeeAlreadyExists() {
        // employee already exists
        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));

        EmployeeException exception = assertThrows(
                EmployeeException.class,
                () -> employeeService.createEmployee(employeeDTO)
        );

        assertEquals("Service.Employee.Found", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    @Test
    void getAllEmployee_Success_ReturnsList() throws EmployeeException {
        Employee employee2 = new Employee();
        employee2.setId("EMP-002");
        employee2.setName("Sippu Sanda");
        employee2.setAge(28);
        employee2.setAddress("123 Maple Street, Springfield, IL");

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee, employee2));

        List<EmployeeDTO> result = employeeService.getAllEmployee();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("EMP-001", result.get(0).getId());
        assertEquals("chammu", result.get(0).getName());
        assertEquals(28, result.get(0).getAge());
        assertEquals("123 Maple Street, Springfield, IL", result.get(0).getAddress());

        assertEquals("EMP-002", result.get(1).getId());
        assertEquals("Sippu Sanda", result.get(1).getName());

        verify(employeeRepository, times(1)).findAll();
    }
    @Test
    void getAllEmployee_ReturnsEmptyList_WhenNoEmployees() throws EmployeeException {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());

        List<EmployeeDTO> result = employeeService.getAllEmployee();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }
    @Test
    void deleteEmployee_Success() throws EmployeeException {
        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        assertDoesNotThrow(() -> employeeService.deleteEmployee("EMP-001"));

        verify(employeeRepository, times(1)).findById("EMP-001");
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void deleteEmployee_ThrowsException_WhenNotFound() {
        when(employeeRepository.findById("EMP-999")).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(
                EmployeeException.class,
                () -> employeeService.deleteEmployee("EMP-999")
        );

        assertEquals("Service.Employee.NotFound", exception.getMessage());
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
    @Test
    void updateEmployee_Success() throws EmployeeException {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId("EMP-001");
        updatedEmployee.setName("chammu");
        updatedEmployee.setAge(28);
        updatedEmployee.setAddress("Bangalore");

        when(employeeRepository.findById("EMP-001")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee("EMP-001", "Bangalore");

        assertNotNull(result);
        assertEquals("EMP-001", result.getId());
        assertEquals("Bangalore", result.getAddress());

        verify(employeeRepository, times(1)).findById("EMP-001");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_ThrowsException_WhenNotFound() {
        when(employeeRepository.findById("EMP-999")).thenReturn(Optional.empty());

        EmployeeException exception = assertThrows(
                EmployeeException.class,
                () -> employeeService.updateEmployee("EMP-999", "Bangalore")
        );

        assertEquals("Service.Employee.NotFound", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
