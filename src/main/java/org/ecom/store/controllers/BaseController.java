package org.ecom.store.controllers;

import org.ecom.store.exceptions.CustomException;

import java.util.function.Supplier;

import static org.ecom.store.exceptions.ExceptionsEnum.GENERAL_ERROR;

public class BaseController {
    protected Supplier<CustomException> throwsGeneralException = () -> new CustomException(GENERAL_ERROR.getCode(), GENERAL_ERROR.getMessage());
}
