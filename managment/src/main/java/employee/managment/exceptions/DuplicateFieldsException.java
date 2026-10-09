package employee.managment.exceptions;

import lombok.Getter;

@Getter
public class DuplicateFieldsException extends RuntimeException {

    private final String field;

    public DuplicateFieldsException(String field, String message) {
        super(message);
        this.field = field;
    }

}
