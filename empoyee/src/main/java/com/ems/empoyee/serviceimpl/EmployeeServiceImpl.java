package com.ems.empoyee.serviceimpl;

import com.ems.empoyee.dto.EmployeeDTO;
import com.ems.empoyee.entity.Employee;
import com.ems.empoyee.exception.EmployeeException;
import com.ems.empoyee.repository.EmployeeRepository;
import com.ems.empoyee.service.EmployeeService;
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
    @Cacheable(value = "employees")
    @Override
    public List<EmployeeDTO> getAllEmployee() {
        log.info("Fetching all employees");
        Iterable <Employee>itr=employeeRepository.findAll();
        List<EmployeeDTO>employeeDTOS=new ArrayList<EmployeeDTO>();
        itr.forEach(employee -> {
            EmployeeDTO employeeDTO=new EmployeeDTO();
            employeeDTO.setAddress(employee.getAddress());
            employeeDTO.setAge(employee.getAge());
            employeeDTO.setName(employee.getName());
            employeeDTO.setId(employee.getId());
            employeeDTOS.add(employeeDTO);
        });
        log.info("All employees data fetched from DB");
        return employeeDTOS;

    }
    @Cacheable(value = "employees",key = "#id")
    @Override
    public EmployeeDTO getEmployeeById(String id) throws EmployeeException {
        log.info("Fetching employee having id from DB");
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> new EmployeeException("Service.Employee.NotFound"));

        EmployeeDTO employeeDTO=new EmployeeDTO();
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setName(employee.getName());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setId(employee.getId());
        return employeeDTO;
    }
    @CacheEvict(value = "employees",key = "#Id")
    @Override
    public void deleteEmployee(String Id) throws EmployeeException{
        Optional<Employee>optional=employeeRepository.findById(Id);
        if(optional.isEmpty()){
            throw new EmployeeException("Service.Employee.NotFound");
        }
        employeeRepository.delete(optional.get());
        log.info("Employee deleted successfully");
    }
   @CachePut(value = "employees",key = "#Id")
    @Override
    public Employee updateEmployee(String Id, String address)throws EmployeeException {
        Optional<Employee>optional=employeeRepository.findById(Id);
        if(optional.isEmpty()){
            throw new EmployeeException("Service.Employee.NotFound");
        }
        optional.get().setAddress(address);
        log.info("Employee updated successfully");
        return  employeeRepository.save(optional.get());

    }

}
