package com.span.acm;

import com.span.acm.dto.EmployeeDTO;
import com.span.acm.entity.Employee;
import com.span.acm.entity.Module;
import com.span.acm.exception.EmployeeNotFoundException;
import com.span.acm.exception.ModuleNotFoundException;
import com.span.acm.repository.EmployeeRepository;
import com.span.acm.repository.ModuleRepository;
import com.span.acm.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    //    Create employee test cases
    @Test
    public void testCreateEmployeeWithNoModules() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Aditya");
        // No modules assigned

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(1L);
            return employee;
        });

        // Act
        Employee createdEmployee = employeeService.createEmployee(employeeDTO);

        // Assert
        assertNotNull(createdEmployee);
        assertEquals("Aditya", createdEmployee.getName());
        assertTrue(createdEmployee.getModules().isEmpty()); // No modules assigned
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithModules() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Aditya");
        Set<Long> moduleIds = new HashSet<>();
        moduleIds.add(1L);
        moduleIds.add(2L);
        employeeDTO.setModuleIds(moduleIds);

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee employee = invocation.getArgument(0);
            employee.setId(2L);
            return employee;
        });

        // Mock ModuleRepository behavior for finding modules by ID
        when(moduleRepository.findById(1L)).thenReturn(Optional.of(new Module(1L, "Library")));
        when(moduleRepository.findById(2L)).thenReturn(Optional.of(new Module(2L, "Dashboard")));

        // Act
        Employee createdEmployee = employeeService.createEmployee(employeeDTO);

        // Assert
        assertNotNull(createdEmployee);
        assertEquals("Aditya", createdEmployee.getName());
        assertEquals(2, createdEmployee.getModules().size());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidModuleId() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("Bob");
        Set<Long> moduleIds = new HashSet<>();
        moduleIds.add(3L); // Module ID that doesn't exist
        employeeDTO.setModuleIds(moduleIds);

        // Mock ModuleRepository behavior for finding modules by ID
        when(moduleRepository.findById(3L)).thenReturn(Optional.empty());

        // Act and Assert
        ModuleNotFoundException exception = assertThrows(ModuleNotFoundException.class,
                () -> employeeService.createEmployee(employeeDTO));

        assertEquals("Module not found with ID: 3", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class)); // Ensure save is not called
    }

//    Update employee test cases

    @Test
    public void testUpdateEmployeeWithModules() {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("UpdatedName");
        Set<Long> moduleIds = new HashSet<>();
        moduleIds.add(1L); // Assuming module ID 1 exists
        employeeDTO.setModuleIds(moduleIds);

        // Mock ModuleRepository behavior for finding a module
        when(moduleRepository.findById(1L)).thenReturn(Optional.of(new Module(1L, "Library")));

        // Mock EmployeeRepository behavior for finding and saving an employee
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("OriginalName");
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.<Employee>getArgument(0));

        // Act
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDTO);

        // Assert
        assertNotNull(updatedEmployee);
        assertEquals("UpdatedName", updatedEmployee.getName());
        assertEquals(1, updatedEmployee.getModules().size()); // Ensure one module is assigned
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeWithEmptyModules() {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("UpdatedName");
        employeeDTO.setModuleIds(null); // No modules specified

        // Mock EmployeeRepository behavior for finding and saving an employee
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("OriginalName");
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.<Employee>getArgument(0));

        // Act
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeDTO);

        // Assert
        assertNotNull(updatedEmployee);
        assertEquals("UpdatedName", updatedEmployee.getName());
        assertEquals(new HashSet<>(), updatedEmployee.getModules()); // No modules assigned
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeWithNonExistingModule() {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("UpdatedName");
        Set<Long> moduleIds = new HashSet<>();
        moduleIds.add(2L); // Assuming module ID 2 does not exist
        employeeDTO.setModuleIds(moduleIds);

        // Mock EmployeeRepository behavior for finding an employee
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("OriginalName");
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        // Mock ModuleRepository behavior for a non-existing module
        when(moduleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act and Assert (expecting ModuleNotFoundException)
        assertThrows(ModuleNotFoundException.class, () -> employeeService.updateEmployee(employeeId, employeeDTO));

        // Ensure that employeeRepository.save() is not called when a module is not found
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    // Delete employee test cases

    @Test
    public void testDeleteEmployee() {
        // Arrange
        Long employeeId = 1L;

        // Mock EmployeeRepository behavior for finding and deleting an employee
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("TestEmployee");
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        doNothing().when(employeeRepository).delete(existingEmployee);

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).delete(existingEmployee);
    }

    @Test
    public void testDeleteNonExistingEmployee() {
        // Arrange
        Long nonExistingEmployeeId = 999L; // Assuming this employee does not exist

        // Mock EmployeeRepository behavior for finding a non-existing employee
        when(employeeRepository.findById(nonExistingEmployeeId)).thenReturn(Optional.empty());

        // Act and Assert (expecting EmployeeNotFoundException)
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(nonExistingEmployeeId));

        // Ensure that employeeRepository.delete() is not called when the employee is not found
        verify(employeeRepository, times(0)).delete(any(Employee.class));
    }
}

