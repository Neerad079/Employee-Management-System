package employee.managment.model;

// A record is a concise way to define a data carrier. Java automatically generates the constructor, accessors, and other methods for you.
public record ApiErrorResponse(int status, String message){}
