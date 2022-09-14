package org.ecom.store.deals;

import lombok.Getter;

import java.util.Arrays;

public enum DealEnum {
    NO_DISCOUNT("NoDiscount"),
    SECOND_ITEM_DISCOUNT("SecondItemDiscount");

    @Getter
    final String className;
    DealEnum(String className) {
        this.className = className;
    }

    private static DealEnum getDealEnum(String className) {
        return Arrays.stream(DealEnum.values()).filter(e -> e.className.equals(className)).findFirst().orElse(NO_DISCOUNT);
    }

    private static String getFullClassName(DealEnum dealEnum) {
        return "org.ecom.store.deals." + dealEnum.className;
    }

    public static String getFullClassNameByClassName(String className) {
        return getFullClassName(getDealEnum(className));
    }
}
