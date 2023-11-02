package hello.jdbc.repository.ex;

public class MyDuplicationException extends MyDBException{

    public MyDuplicationException() {
    }

    public MyDuplicationException(String message) {
        super(message);
    }

    public MyDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicationException(Throwable cause) {
        super(cause);
    }
}
