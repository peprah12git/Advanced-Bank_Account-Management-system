package exceptions;

// 1. Custom exception for viewing Test.accounts
public class ViewAccountException extends Exception {
    public ViewAccountException() {
        super("No Test.accounts available to view.");
    }

    public ViewAccountException(String message) {
        super(message);
    }
}
