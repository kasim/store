package org.ecom.store.exceptions;

public class NotEnoughQuantityInBasketException extends CustomException {

    public NotEnoughQuantityInBasketException(int code, String message) {
        super(code, message);
    }
}
