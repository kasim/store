package org.ecom.store.services;

import lombok.val;
import org.ecom.store.deals.DealEnum;
import org.ecom.store.models.Product;
import org.ecom.store.models.Item;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import static org.ecom.store.data.Store.STOCK;

@Service
public class StoreService extends BaseService{
    public Optional<Item> create(Item item) {
        val product = Optional.ofNullable(item.getProduct()).orElseThrow();
        val deal = Optional.ofNullable(product.getDeal());
        if(deal.isPresent()) {
            if (Arrays.stream(DealEnum.values()).noneMatch(d -> d.getClassName().equals(deal.get().getStrategy()))) {
                throw throwNoDiscountStrategyException.get();
            }
            val discount = deal.get().getDiscount();
            if(discount == null) {
                throw throwNoDiscountRateException.get();
            } else {
                if (discount.compareTo(BigDecimal.ONE) > 0 || discount.compareTo(BigDecimal.ZERO) <=0 ) {
                    throw throwWrongDiscountRateException.get();
                }
            }
        }
        var quantity = Optional.ofNullable(STOCK.get(product)).orElse(BigInteger.ZERO);
        quantity = quantity.add(item.getQuantity());
        val createdItem = Optional.ofNullable(Item.builder().product(product).quantity(quantity).build());
        if(STOCK.keySet().stream().anyMatch(p -> p.getId() == item.getProduct().getId())
                && !STOCK.containsKey(product)) {
            throw throwDuplicatedProductIdException.get();
        }
        STOCK.put(item.getProduct(), quantity);
        return createdItem;
    }

    public Optional<Product> delete(long id) {
        val product = STOCK.keySet().stream().filter(p -> p.getId() == id).findFirst()
                .orElseThrow(throwProductNotFoundException);
        return STOCK.remove(product).compareTo(BigInteger.ZERO) >= 0 ? Optional.of(product) : Optional.empty();
    }
}
