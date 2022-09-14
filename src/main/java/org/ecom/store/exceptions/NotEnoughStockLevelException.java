package org.ecom.store.exceptions;

public class NotEnoughStockLevelException extends CustomException{
    public NotEnoughStockLevelException(int code, String message){
        super(code, message);
    }
}
