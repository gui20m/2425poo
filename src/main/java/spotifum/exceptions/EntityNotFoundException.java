package spotifum.exceptions;

/**
 * The type Entity not found exception.
 */
public class EntityNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Entity not found exception.
     */
    public EntityNotFoundException(){ super();}

    /**
     * Instantiates a new Entity not found exception.
     *
     * @param message the message
     */
    public EntityNotFoundException(String message){ super(message);}
}
