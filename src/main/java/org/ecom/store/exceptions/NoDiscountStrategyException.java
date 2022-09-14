package org.ecom.store.exceptions;

public class NoDiscountStrategyException extends CustomException{
    public NoDiscountStrategyException(int code, String message) {
        super(code, message);
    }
}
