package Exceptions;

public class AuthenticationFailedException extends Exception{
    public AuthenticationFailedException(){
        super("Message authentication failed");
    }
}
