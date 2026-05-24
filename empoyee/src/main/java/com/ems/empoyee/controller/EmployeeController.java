package com.ems.empoyee.controller;

import com.ems.empoyee.dto.EmployeeDTO;
import com.ems.empoyee.entity.Employee;
import com.ems.empoyee.exception.EmployeeException;
import com.ems.empoyee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    Environment environment;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws EmployeeException {
        employeeService.createEmployee(employeeDTO);
        String message = environment.getProperty("API.Creation_successful");
        return new ResponseEntity<>(message,
                HttpStatus.CREATED);
    }
    @GetMapping("/employees")
        public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployee(),HttpStatus.OK);
    }
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id)throws EmployeeException {
        return new ResponseEntity<>(employeeService.getEmployeeById(id),HttpStatus.OK);
    }
    @PutMapping("/{id}/{address}")
    public ResponseEntity<Employee>updateEmployee(@PathVariable String Id, @PathVariable String address)throws EmployeeException {
        return new ResponseEntity<>(employeeService.updateEmployee(Id,address),HttpStatus.OK);
    }
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String>deleteEmployee(@PathVariable String id)throws EmployeeException {

        employeeService.deleteEmployee(id);
        String message = environment.getProperty("API.Deleted_successfully");
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
