package org.ecom.store.exceptions;

public class DuplicatedProductIdException extends CustomException {
    public DuplicatedProductIdException(int code, String message) {
        super(code, message);
    }
}
