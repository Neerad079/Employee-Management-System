package employee.managment.controller;


import employee.managment.model.employeeDB;
import employee.managment.services.employeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@AllArgsConstructor

public class employee {
    private final employeeService service;

    @GetMapping
    public ResponseEntity<List<employeeDB>> getAll(){
        return ResponseEntity.ok(service.getAllEmployee());
    }
    @GetMapping("/{id}")
    public ResponseEntity<employeeDB> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getEmployeeById(id));
    }
    @GetMapping("/search")
    public ResponseEntity<List<employeeDB>> search(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(service.searchEmployee(keyword));
    }
    @PostMapping
    public ResponseEntity<employeeDB> createEmployee(@RequestBody employeeDB employeeDB ){
        return ResponseEntity.ok(service.createEmployee(employeeDB));
    }
}
