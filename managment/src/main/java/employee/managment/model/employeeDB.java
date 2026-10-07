package employee.managment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class employeeDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, unique = true)
    private Integer employeeCode;

    @com.fasterxml.jackson.annotation.JsonAlias({"firstname", "Firstname"})
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNo;
    private String department;
    private String designation;
    private Long salary;
    private LocalDate joiningDate;

    @Column(name = "status_")   // keeps your existing column name
    @com.fasterxml.jackson.annotation.JsonAlias("status_")
    private String status;
}
