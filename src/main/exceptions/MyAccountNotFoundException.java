package main.exceptions;

public class MyAccountNotFoundException extends Exception {

    public MyAccountNotFoundException(String message) {
        super(message);
    }
}
