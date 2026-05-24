package com.ems.empoyee.service;

import com.ems.empoyee.dto.EmployeeDTO;
import com.ems.empoyee.entity.Employee;
import com.ems.empoyee.exception.EmployeeException;

import java.util.List;

public interface EmployeeService {
    void createEmployee(EmployeeDTO employeeDTO) throws EmployeeException;
    List<EmployeeDTO> getAllEmployee();
    EmployeeDTO getEmployeeById(String id) throws EmployeeException;
    Employee updateEmployee(String Id, String address) throws EmployeeException;
    void deleteEmployee(String Id) throws EmployeeException;


}