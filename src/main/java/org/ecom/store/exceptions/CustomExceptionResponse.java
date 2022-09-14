package org.ecom.store.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomExceptionResponse {
    private int code;
    private String message;
}
