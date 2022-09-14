package org.ecom.store.services;

import org.ecom.store.exceptions.*;

import java.util.function.Supplier;

import static org.ecom.store.exceptions.ExceptionsEnum.*;

public class BaseService {
    protected Supplier<DuplicatedProductIdException> throwDuplicatedProductIdException = () -> new DuplicatedProductIdException(DUPLICATED_PRODUCT_ID.getCode(), DUPLICATED_PRODUCT_ID.getMessage());
    protected Supplier<ProductNotFoundException> throwProductNotFoundException = () -> new ProductNotFoundException(PRODUCT_NOT_FOUND.getCode(), PRODUCT_NOT_FOUND.getMessage());
    protected Supplier<NotEnoughStockLevelException> throwNotEnoughStockLevelException = () -> new NotEnoughStockLevelException(NOT_ENOUGH_QUANTITY_FOR_SELL.getCode(), NOT_ENOUGH_QUANTITY_FOR_SELL.getMessage());
    protected Supplier<ProductNotInBasketException> throwNotInBasketException = () -> new ProductNotInBasketException(PRODUCT_NOT_FOUND_IN_BASKET.getCode(), PRODUCT_NOT_FOUND_IN_BASKET.getMessage());
    protected Supplier<NotEnoughQuantityInBasketException> throwNotEnoughQuantityInBasketException = () -> new NotEnoughQuantityInBasketException(NOT_ENOUGH_QUANTITY_FOR_REMOVE.getCode(), NOT_ENOUGH_QUANTITY_FOR_REMOVE.getMessage());
    protected Supplier<NoBasketException> throwNoBasketException = () -> new NoBasketException(USER_HAS_NO_BASKET.getCode(), USER_HAS_NO_BASKET.getMessage());
    protected Supplier<NoDiscountStrategyException> throwNoDiscountStrategyException = () -> new NoDiscountStrategyException(NO_MATCHED_STRATEGY_NAME.getCode(), NO_MATCHED_STRATEGY_NAME.getMessage());
    protected Supplier<NoDiscountRateSpecifiedException> throwNoDiscountRateException = () -> new NoDiscountRateSpecifiedException(NO_DISCOUNT_RATE.getCode(), NO_DISCOUNT_RATE.getMessage());
    protected Supplier<WrongDiscountRateException> throwWrongDiscountRateException = () -> new WrongDiscountRateException(WRONG_DISCOUNT_RATE.getCode(), WRONG_DISCOUNT_RATE.getMessage());
    protected Supplier<CustomException> throwGeneralException = () -> new CustomException(GENERAL_ERROR.getCode(), GENERAL_ERROR.getMessage());
}
