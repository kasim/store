package org.ecom.store.deals;

import lombok.Setter;
import lombok.val;
import org.ecom.store.models.ItemSubTotal;
import org.ecom.store.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

@Component
@Setter
public class SecondItemDiscount extends AbstractDealHandler {

    @Override
    public ItemSubTotal handler(Product product, BigInteger quantity) {
        val discountPriceQuantity = quantity.divide(BigInteger.TWO);
        val originalPriceQuantity = quantity.subtract(discountPriceQuantity);
        val productPrice = product.getPrice();
        val originalPriceProduct = productPrice.multiply(BigDecimal.valueOf(originalPriceQuantity.longValue()));
        val discountPriceProduct = productPrice.multiply(discount).multiply(BigDecimal.valueOf(discountPriceQuantity.longValue()));
        return ItemSubTotal.builder()
                .product(product)
                .quantity(quantity)
                .unitSubTotal(originalPriceProduct.add(discountPriceProduct))
                .build();
    }
}
