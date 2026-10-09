package employee.managment.services;

import employee.managment.exceptions.DuplicateFieldsException;
import employee.managment.exceptions.EmployeeStatusException;
import employee.managment.exceptions.employeeNotFoundException;
import employee.managment.model.employeeDB;
import employee.managment.repository.employeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class employeeService {
    private final employeeRepo repo;


    public List<employeeDB> getAllEmployee(){
        return repo.findAll();
    }
    public employeeDB getEmployeeById(Long id){
        return repo.findById(id)
                .orElseThrow(()->new employeeNotFoundException(
                        "Employee with id "+id+" not found"
                ));
        
    }
    public List<employeeDB> searchEmployee(String keyword){
        return repo.searchEmployees(keyword);
    }
    public employeeDB createEmployee(employeeDB emp) {
        employeeDB employee;
        if(repo.existsByEmployeeCode(emp.getEmployeeCode())){
            throw new DuplicateFieldsException(
                    "employeeCode",
                    "Employee code already exists");
        }
        if (repo.existsByEmail(emp.getEmail())) {
            throw new DuplicateFieldsException(
                    "email",
                    "Email already exists"
            );
        }
        if(! (emp.getStatus().equals("Active")|| emp.getStatus().equals("Inactive") )){
            throw new EmployeeStatusException("Employee Status Must be either Active or Inactive");
        }
        employee = employeeDB.builder()
                    .employeeCode(emp.getEmployeeCode())
                    .firstName(emp.getFirstName())
                    .lastName(emp.getLastName())
                    .email(emp.getEmail())
                    .phoneNo(emp.getPhoneNo())
                    .department(emp.getDepartment())
                    .designation(emp.getDesignation())
                    .salary(emp.getSalary())
                    .joiningDate(emp.getJoiningDate())
                    .status(emp.getStatus())
                    .build();

            System.out.println(employee);   // @Data generates toString()
            return repo.save(employee);

    }
    public employeeDB updateEmployee(Long id,employeeDB emp){
        employeeDB employee = repo.findById(id)
                .orElseThrow(()->new RuntimeException("Employee not found with id: "+id));
        employee.setEmployeeCode(emp.getEmployeeCode());
        employee.setFirstName(emp.getFirstName());
        employee.setLastName(emp.getLastName());
        employee.setEmail(emp.getEmail());
        employee.setPhoneNo(emp.getPhoneNo());
        employee.setDepartment(emp.getDepartment());
        employee.setDesignation(emp.getDesignation());
        employee.setSalary(emp.getSalary());
        employee.setJoiningDate(emp.getJoiningDate());
        employee.setStatus(emp.getStatus());
        return repo.save(employee);
    }
    public void deleteEmployee(Long id){
        repo.deleteById(id);
    }
}
