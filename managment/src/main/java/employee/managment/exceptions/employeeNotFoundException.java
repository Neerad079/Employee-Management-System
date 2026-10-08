package employee.managment.exceptions;

public class employeeNotFoundException extends RuntimeException {
    public employeeNotFoundException(String message){
        super(message);
    }
}
