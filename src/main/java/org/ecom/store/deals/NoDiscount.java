package org.ecom.store.deals;

import org.ecom.store.models.ItemSubTotal;
import org.ecom.store.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
public class NoDiscount extends AbstractDealHandler {

    @Override
    public ItemSubTotal handler(Product product, BigInteger quantity) {
        return ItemSubTotal.builder()
                .product(product)
                .quantity(quantity)
                .unitSubTotal(product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity.longValue())))
                .build();
    }
}
