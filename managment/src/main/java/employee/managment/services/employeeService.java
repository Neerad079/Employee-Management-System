package employee.managment.services;

import employee.managment.model.employeeDB;
import employee.managment.repository.employeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class employeeService {
    private final employeeRepo repo;


    public List<employeeDB> getAllEmployee(){
        return repo.findAll();
    }
    public employeeDB getEmployeeById(Long Id){
        return repo.findById(Id)
                .orElseThrow(()->new RuntimeException("Employee not found with id: "+Id));
        
    }
    public List<employeeDB> searchEmployee(String keyword){
        return repo.searchEmployees(keyword);
    }
    public employeeDB createEmployee(employeeDB emp){
        employeeDB employee = employeeDB.builder()
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
//    public employeeDB updateEmployee(Long id,employeeDB emp){
//        employeeDB employee = repo.findById(id)
//                .orElseThrow(()->new RuntimeException("Employee not found with id: "+id));
//        employee.setEmployeeCode(emp.getEmployeeCode());
//        employee.setFirstname(emp.getfirstname());
//        employee.setLastName(emp.getLastName());
//        employee.setEmail(emp.getEmail());
//        employee.setPhoneNo(emp.getPhoneNo());
//        employee.setDepartment(emp.getDepartment());
//        employee.setDesignation(emp.getDesignation());
//        employee.setSalary(emp.getSalary());
//        employee.setJoiningDate(emp.getJoiningDate());
//        employee.setStatus_(emp.getStatus_());
//        return repo.save(employee);
//    }
    public void deleteEmployee(Long id){
        repo.deleteById(id);
    }
}
