package org.ecom.store.exceptions;

public class NoDiscountRateSpecifiedException extends CustomException {
    public NoDiscountRateSpecifiedException(int code, String message) {
        super(code, message);
    }
}
