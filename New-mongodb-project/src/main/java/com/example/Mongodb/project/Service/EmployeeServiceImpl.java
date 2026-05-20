package com.example.Mongodb.project.Service;

import com.example.Mongodb.project.DTO.EmployeeDTO;
import com.example.Mongodb.project.Entity.Employee;
import com.example.Mongodb.project.Exception.EmployeeException;
import com.example.Mongodb.project.Repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log= LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private static final String CACHE_NAME="employee";
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void createEmployee(EmployeeDTO employeeDTO)throws EmployeeException {
        Optional<Employee>optional=employeeRepository.findById(employeeDTO.getId());
       if(optional.isPresent()){
           throw new EmployeeException("Service.Employee.Found");
        }
        log.info("Saving Employee");
       Employee employee=new Employee();
       employee.setAddress(employeeDTO.getAddress());
       employee.setAge(employeeDTO.getAge());
       employee.setName(employeeDTO.getName());
       employee.setId(employeeDTO.getId());
       employeeRepository.save(employee);
        log.info("Employee saved successfully");
    }
    @Cacheable
    @Override
    public List<EmployeeDTO> getAllEmployee() {
        log.info("Fetching all employees");
        Iterable <Employee>itr=employeeRepository.findAll();
        List<EmployeeDTO>employeeDTOS=new ArrayList<EmployeeDTO>();
        itr.forEach(employee -> {
            EmployeeDTO employeeDTO=new EmployeeDTO();
            employeeDTO.setAddress(employee.getAddress());
            employeeDTO.setAge(employee.getAge());
            employeeDTO.setId(employee.getId());
            employeeDTOS.add(employeeDTO);
        });
        log.info("All employees data fetched");
        return employeeDTOS;

    }

    @Override
    @Cacheable
    public EmployeeDTO getEmployeeById(String id) throws EmployeeException {
        log.info("Fetching employee having id");
        Optional<Employee>optional=employeeRepository.findById(id);
        if(optional.isEmpty()){
            throw new EmployeeException("Service.Employee.NotFound");
        }
        Employee employee=optional.get();
        EmployeeDTO employeeDTO=new EmployeeDTO();
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setName(employee.getName());
        employeeDTO.setAddress(employee.getAddress());
        return employeeDTO;
    }

    @Override
    @CachePut
    public Employee updateEmployee(String Id, String address)throws EmployeeException {
        Optional<Employee>optional=employeeRepository.findById(Id);
        if(optional.isEmpty()){
            throw new EmployeeException("Service.Employee.NotFound");
        }
        optional.get().setAddress(address);
        log.info("Employee updated successfully");
        return  employeeRepository.save(optional.get());

    }

    @Override
    @CacheEvict
    public void deleteEmployee(String Id) throws EmployeeException{
        Optional<Employee>optional=employeeRepository.findById(Id);
        if(optional.isEmpty()){
            throw new EmployeeException("Service.Employee.NotFound");
        }
        employeeRepository.delete(optional.get());
        log.info("Employee deleted successfully");
    }
}
