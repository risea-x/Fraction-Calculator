package fractionexception;


/**
 * The class Mixed Fraction Exception represents an exception.
 * @author Neil Briones
 * @author Clarence Cayton
 * @author Junsel Fabe
 * @author Dylan Magpili
 * @author Hans Viduya
 * @version JDK 19
 */

public class MixedFractionException extends Exception  {

    /**
     * Mixed Fraction Exception constructor that shows message.
     * @param message the message that would show message error
     */
    public MixedFractionException(String message){
        super(message);
    }

    /**
     * Mixed Fraction Exception constructor with two parameters.
     * @param message the statement that would show message error
     * @param cause the throwable object
     */
    public MixedFractionException(String message, Throwable cause){
        super(message, cause);
    }

}
