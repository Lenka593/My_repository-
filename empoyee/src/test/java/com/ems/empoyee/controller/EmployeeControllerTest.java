package com.ems.empoyee.controller;
import com.ems.empoyee.dto.EmployeeDTO;
import com.ems.empoyee.entity.Employee;
import com.ems.empoyee.exception.EmployeeException;
import com.ems.empoyee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(EmployeeController.class)
@TestPropertySource(properties = {
        "API.Creation_successful=Employee created successfully",
        "API.Deleted_successfully=Employee deleted successfully"
})
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;                    // simulates HTTP requests without starting real server

    @MockitoBean
    private EmployeeService employeeService;    // mock service — no real DB calls

    @Autowired
    private ObjectMapper objectMapper;          // converts objects to JSON string

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
    void createEmployee_Success_Returns201() throws Exception {
        doNothing().when(employeeService).createEmployee(any(EmployeeDTO.class));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Employee created successfully"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
    }

    @Test
    void createEmployee_ThrowsException_WhenEmployeeAlreadyExists() throws Exception {
        doThrow(new EmployeeException("Service.Employee.Found"))
                .when(employeeService).createEmployee(any(EmployeeDTO.class));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).createEmployee(any(EmployeeDTO.class));
    }
    @Test
    void getAllEmployees_Success_Returns200() throws Exception {
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setId("EMP-002");
        employeeDTO2.setName("Sippu Sanda");
        employeeDTO2.setAge(28);
        employeeDTO2.setAddress("123 Maple Street, Springfield, IL");

        List<EmployeeDTO> employeeList = Arrays.asList(employeeDTO, employeeDTO2);
        when(employeeService.getAllEmployee()).thenReturn(employeeList);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("EMP-001"))
                .andExpect(jsonPath("$[0].name").value("chammu"))
                .andExpect(jsonPath("$[1].id").value("EMP-002"))
                .andExpect(jsonPath("$[1].name").value("Sippu Sanda"));

        verify(employeeService, times(1)).getAllEmployee();
    }

    @Test
    void getAllEmployees_ReturnsEmptyList_Returns200() throws Exception {
        when(employeeService.getAllEmployee()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(employeeService, times(1)).getAllEmployee();
    }
    @Test
    void getEmployeeById_Success_Returns200() throws Exception {
        when(employeeService.getEmployeeById("EMP-001")).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/EMP-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("EMP-001"))
                .andExpect(jsonPath("$.name").value("chammu"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.address").value("123 Maple Street, Springfield, IL"));

        verify(employeeService, times(1)).getEmployeeById("EMP-001");
    }

    @Test
    void getEmployeeById_ThrowsException_WhenNotFound() throws Exception {
        when(employeeService.getEmployeeById("EMP-999"))
                .thenThrow(new EmployeeException("Service.Employee.NotFound"));

        mockMvc.perform(get("/api/employees/EMP-999"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).getEmployeeById("EMP-999");
    }

    @Test
    void updateEmployee_Success_Returns200() throws Exception {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId("EMP-001");
        updatedEmployee.setName("chammu");
        updatedEmployee.setAge(28);
        updatedEmployee.setAddress("Bangalore");

        when(employeeService.updateEmployee("EMP-001", "Bangalore")).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/EMP-001/Bangalore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("EMP-001"))
                .andExpect(jsonPath("$.address").value("Bangalore"));

        verify(employeeService, times(1)).updateEmployee("EMP-001", "Bangalore");
    }

    @Test
    void updateEmployee_ThrowsException_WhenNotFound() throws Exception {
        when(employeeService.updateEmployee("EMP-999", "Bangalore"))
                .thenThrow(new EmployeeException("Service.Employee.NotFound"));

        mockMvc.perform(put("/api/EMP-999/Bangalore"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).updateEmployee("EMP-999", "Bangalore");
    }
    @Test
    void deleteEmployee_Success_Returns200() throws Exception {
        doNothing().when(employeeService).deleteEmployee("EMP-001");

        mockMvc.perform(delete("/api/employees/EMP-001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployee("EMP-001");
    }

    @Test
    void deleteEmployee_ThrowsException_WhenNotFound() throws Exception {
        doThrow(new EmployeeException("Service.Employee.NotFound"))
                .when(employeeService).deleteEmployee("EMP-999");

        mockMvc.perform(delete("/api/employees/EMP-999"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).deleteEmployee("EMP-999");
    }
}
