package M6FGR.mapi.cls.exceptions;

public class ClassLoadingException extends RuntimeException {

    public ClassLoadingException(String message) {
        super(message);
    }

    public ClassLoadingException(Throwable cause) {
        super(cause);
    }

    public ClassLoadingException() {
        super();
    }
}
