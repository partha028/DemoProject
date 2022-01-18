package com.infosys.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.employee.exception.EmployeeNotFoundException;
import com.infosys.employee.model.Employee;
import com.infosys.employee.repository.EmployeeRepository;
import com.infosys.employee.service.EmployeeService;

@RestController
@RequestMapping("/Infosys")
public class EmployeeController {
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/employees")
	public List<Employee> getEmployee(){
		return employeeRepository.findAll();
	}
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity <Employee> getEmployeeById(@PathVariable long employeeId) throws EmployeeNotFoundException{
	Optional <Employee> employee = employeeRepository.findById(employeeId);
	if(employee.isPresent())
		return new ResponseEntity<>(employee.get(),HttpStatus.OK);
	else
		throw new EmployeeNotFoundException("Employee not found for this id : "+ employeeId);
	}
	@PostMapping("/saveEmployee")
	public Employee save(@RequestBody Employee employee) {
		employee.setEmployeeId(employeeService.getSequenceNumber(Employee.SEQUENCE_NAME));
		return employeeRepository.save(employee);		
	}
	@PutMapping("/update/{employeeId}")
	public ResponseEntity <Employee> updateEmployee(@PathVariable long employeeId,@RequestBody Employee employeeDTO) throws EmployeeNotFoundException{
		Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this id : " + employeeId));
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setSalary(employeeDTO.getSalary());
        employee.setAddress(employeeDTO.getAddress());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);        
	}
	@DeleteMapping("/delete/{employeeId}")
	public Map<String, Boolean> deleteEmployee(@PathVariable long employeeId) throws EmployeeNotFoundException{
		Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for this id : " + employeeId));
		 employeeRepository.delete(employee);
	     Map<String, Boolean> response = new HashMap<>();
	     response.put("deleted", Boolean.TRUE);
	     return response;
	}

}
