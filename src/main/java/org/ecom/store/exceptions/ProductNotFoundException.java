package org.ecom.store.exceptions;

public class ProductNotFoundException extends CustomException {

    public ProductNotFoundException(int code, String message) {
        super(code, message);
    }
}
