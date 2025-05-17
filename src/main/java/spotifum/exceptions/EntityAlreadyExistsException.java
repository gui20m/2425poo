package spotifum.exceptions;

/**
 * The type Entity already exists exception.
 */
public class EntityAlreadyExistsException extends RuntimeException{
    /**
     * Instantiates a new Entity already exists exception.
     */
    public EntityAlreadyExistsException(){ super();}

    /**
     * Instantiates a new Entity already exists exception.
     *
     * @param exception the exception
     */
    public EntityAlreadyExistsException(String exception){ super(exception);}
}
