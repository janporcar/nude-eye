package persistence.exception;





public class PersistenceFileNotFoundException extends PersistenceException {

    




    public PersistenceFileNotFoundException(String message) {
        super(message);
    }

    





    public PersistenceFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
