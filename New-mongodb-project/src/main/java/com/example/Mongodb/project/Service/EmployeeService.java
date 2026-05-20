package com.example.Mongodb.project.Service;

import com.example.Mongodb.project.DTO.EmployeeDTO;
import com.example.Mongodb.project.Entity.Employee;
import com.example.Mongodb.project.Exception.EmployeeException;

import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeDTO employeeDTO) throws EmployeeException;
    List<EmployeeDTO> getAllEmployee();
    EmployeeDTO getEmployeeById(String id) throws EmployeeException;
    Employee updateEmployee(String Id, String address) throws EmployeeException;
    void deleteEmployee(String Id) throws EmployeeException;


}