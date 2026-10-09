package employee.managment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Column(name = "first_name", nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            // ^ - start of the input , [6-9] First digits must be b/w 6 and 9
            // {9} - of 9 length , $ - end of string (no more char allowed)
            message = "Phone number must be a valid 10 digit phone number"
    )
    private String phoneNo;

    private String department;
    private String designation;
    private Long salary;

    @NotNull
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @Column(name = "status_")
    @com.fasterxml.jackson.annotation.JsonAlias("status_")
    private String status;
}
