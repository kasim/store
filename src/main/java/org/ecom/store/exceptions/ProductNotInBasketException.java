package org.ecom.store.exceptions;

public class ProductNotInBasketException extends CustomException {

    public ProductNotInBasketException(int code, String message) {
        super(code, message);
    }
}
