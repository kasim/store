package org.ecom.store.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.ecom.store.exceptions.ExceptionsEnum.GENERAL_ERROR;

@ControllerAdvice
public class RestfulExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler({NoDiscountStrategyException.class, NoDiscountStrategyException.class, WrongDiscountRateException.class,
            NoBasketException.class, NotEnoughQuantityInBasketException.class, ProductNotInBasketException.class,
            DuplicatedProductIdException.class, ProductNotFoundException.class, NotEnoughStockLevelException.class})
    protected CustomExceptionResponse handleException(CustomException ce) {
        return CustomExceptionResponse.builder().code(ce.getCode()).message(ce.getMessage()).build();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected  CustomExceptionResponse handleException() {
        return CustomExceptionResponse.builder().code(GENERAL_ERROR.getCode()).message(GENERAL_ERROR.getMessage()).build();
    }
}
