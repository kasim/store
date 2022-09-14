package org.ecom.store.exceptions;

public class WrongDiscountRateException extends CustomException {
    public WrongDiscountRateException(int code, String message) {
        super(code, message);
    }
}
