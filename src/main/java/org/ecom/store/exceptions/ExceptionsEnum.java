package org.ecom.store.exceptions;

import lombok.Getter;
import org.ecom.store.deals.DealEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum ExceptionsEnum {
    GENERAL_ERROR(400, "System error!"),
    DUPLICATED_PRODUCT_ID(401, "Duplicated product identifier!"),
    PRODUCT_NOT_FOUND(402, "Cannot found the product!"),
    NOT_ENOUGH_QUANTITY_FOR_SELL(403, "Not enough stock level!"),
    USER_HAS_NO_BASKET(404, "User does not have a basket!"),
    PRODUCT_NOT_FOUND_IN_BASKET(405, "Cannot found the product in the basket!"),
    NOT_ENOUGH_QUANTITY_FOR_REMOVE(406, "Not enough quantity of product in basket!"),
    NO_MATCHED_STRATEGY_NAME(407, "No matching discount strategy name matched! Strategy name should be " + Arrays.stream(DealEnum.values()).map(DealEnum::getClassName).collect(Collectors.toList())),
    NO_DISCOUNT_RATE(408, "No discount rate specified!"),
    WRONG_DISCOUNT_RATE(409, "Discount rate should be between 0.0 and 1.0!");


    private final int code;
    private final String message;

    ExceptionsEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
