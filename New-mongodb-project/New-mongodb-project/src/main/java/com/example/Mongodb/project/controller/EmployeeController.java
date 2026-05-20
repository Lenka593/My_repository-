package com.example.Mongodb.project.controller;

import com.example.Mongodb.project.DTO.EmployeeDTO;
import com.example.Mongodb.project.Exception.EmployeeException;
import com.example.Mongodb.project.Service.EmployeeService;
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
    // private static final Logger log= LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    Environment environment;
    @Autowired
    private EmployeeService employeeservice;

    @PostMapping("/employee")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws EmployeeException {
         employeeservice.createEmployee(employeeDTO);
        String message = environment.getProperty("API.Creation_successful");
        return new ResponseEntity<>(message,
                HttpStatus.CREATED);
    }
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>>getAllEmployee() {
   return new ResponseEntity<>(employeeservice.getAllEmployee(),HttpStatus.OK);
    }
    @GetMapping("/employees/{Id}")
    public ResponseEntity<EmployeeDTO>getAllEmployee(@PathVariable String Id)throws EmployeeException {
        return new ResponseEntity<>(employeeservice.getEmployeeById(Id),HttpStatus.OK);
    }
}
