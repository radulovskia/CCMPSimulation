package Exceptions;

public class XorLengthException extends Exception{
    public XorLengthException(){
        super("The byte sequences have different lengths.");
    }
}
