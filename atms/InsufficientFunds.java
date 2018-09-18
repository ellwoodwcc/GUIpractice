package atms;


public class InsufficientFunds extends Exception{

    /* ----------Constructor only is needed to push the quote------- */
    public InsufficientFunds(){
        super("Insufficient Funds");//the super calls the exception method with a string.
        //it apparently can be called in a constructor with a different parameter type/length
    }
}
