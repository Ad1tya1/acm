package com.span.acm.service;

import com.span.acm.dto.EmployeeDTO;
import com.span.acm.entity.Employee;
import com.span.acm.entity.Module;
import com.span.acm.exception.EmployeeNotFoundException;
import com.span.acm.exception.ModuleNotFoundException;
import com.span.acm.repository.EmployeeRepository;
import com.span.acm.repository.ModuleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModuleRepository moduleRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Employee not found with ID: " + id;
                    log.error(errorMessage);
                    return new EmployeeNotFoundException(errorMessage);
                });
    }

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());

        // Assign modules to the employee (if provided)
        if (employeeDTO.getModuleIds() != null) {
            Set<Module> modules = new HashSet<>();
            for (Long moduleId : employeeDTO.getModuleIds()) {
                Module module = moduleRepository.findById(moduleId)
                        .orElseThrow(() -> {
                            String errorMessage = "Module not found with ID: " + moduleId;
                            log.error(errorMessage);
                            return new ModuleNotFoundException(errorMessage);
                        });
                modules.add(module);
            }
            employee.setModules(modules);
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setName(employeeDTO.getName());

        // Update assigned modules
        if (employeeDTO.getModuleIds() != null) {
            Set<Module> modules = new HashSet<>();
            for (Long moduleId : employeeDTO.getModuleIds()) {
                Module module = moduleRepository.findById(moduleId)
                        .orElseThrow(() -> {
                            String errorMessage = "Module not found with ID: " + moduleId;
                            log.error(errorMessage);
                            return new ModuleNotFoundException(errorMessage);
                        });
                modules.add(module);
            }
            existingEmployee.setModules(modules);
        }

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeById(id);
        employeeRepository.delete(existingEmployee);
    }
}
