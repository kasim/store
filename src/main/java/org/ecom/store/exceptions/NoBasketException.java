package org.ecom.store.exceptions;

public class NoBasketException extends CustomException {

    public NoBasketException(int code, String message) {
        super(code, message);
    }
}
