package M6FGR.mapi.cls.exceptions;

public class IllegalSideException extends RuntimeException {

    public IllegalSideException(String message) {
        super(message);
    }

    public IllegalSideException(Throwable cause) {
        super(cause);
    }

    public IllegalSideException() {
        super();
    }
}
