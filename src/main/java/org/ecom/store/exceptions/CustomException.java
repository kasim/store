package org.ecom.store.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class CustomException extends RuntimeException {
    private final int code;
    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }
}
