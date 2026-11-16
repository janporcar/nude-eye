package persistence.exception;




public class PersistenceWriteException extends PersistenceException {

    




    public PersistenceWriteException(String message) {
        super(message);
    }

    





    public PersistenceWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
