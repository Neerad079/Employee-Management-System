package employee.managment.exceptions;

public class DuplicateFieldsException extends RuntimeException{
    public DuplicateFieldsException(String message){
        super(message);
    }
}
