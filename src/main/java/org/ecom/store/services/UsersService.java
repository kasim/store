package org.ecom.store.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.ecom.store.deals.AbstractDealHandler;
import org.ecom.store.deals.DealEnum;
import org.ecom.store.deals.NoDiscount;
import org.ecom.store.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.ecom.store.data.Store.BASKET;
import static org.ecom.store.data.Store.STOCK;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersService extends BaseService {
    private final ApplicationContext context;

    public Optional<Basket> addItem(UserItem item) {
        val productIdToBuy = Optional.of(item.getProductId()).orElse(BigInteger.ONE.negate().longValue());
        val productToBuy = STOCK.keySet().stream()
                .filter(p -> p.getCurrency().equals(item.getUser().getCurrency()))
                .filter(p -> p.getId() == productIdToBuy).findFirst().orElseThrow(throwProductNotFoundException);
        STOCK.computeIfPresent(productToBuy, (p, q) -> {
            if(q.compareTo(item.getQuantity()) >= 0) {
                return q.subtract(item.getQuantity());
            } else {
                throw throwNotEnoughStockLevelException.get();
            }
        });
        if (BASKET.containsKey(item.getUser())) {
            BASKET.computeIfPresent(item.getUser(), (u, i) -> {
                if (i.putIfAbsent(productToBuy, item.getQuantity()) != null) {
                    i.computeIfPresent(productToBuy, (p, q) -> q.add(item.getQuantity()));
                }
                return i;
            });
        } else {
            ConcurrentHashMap<Product, BigInteger> newMap = new ConcurrentHashMap<>();
            newMap.putIfAbsent(productToBuy, item.getQuantity());
            BASKET.putIfAbsent(item.getUser(), newMap);
        }
        return Optional.of(Basket.builder().user(item.getUser()).items(BASKET.get(item.getUser())).build());
    }

    public Optional<Basket> removeItem(long userId, long productId, BigInteger quantity) {
        val user = BASKET.keySet().stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(throwNoBasketException);
        val productToRemove = BASKET.get(user).keySet().stream().filter(p -> p.getId() == productId).findFirst().orElseThrow(throwNotInBasketException);
        BASKET.computeIfPresent(user, (u, i) -> {
            i.computeIfPresent(productToRemove, (p, q) -> {
                if (q.compareTo(quantity) > 0) {
                    return q.subtract(quantity);
                } else if (q.compareTo(quantity) == 0) {
                    i.remove(p);
                    return q;
                } else {
                    throw throwNotEnoughQuantityInBasketException.get();
                }
            });
            return i;
        });
        return Optional.of(Basket.builder().user(user).items(BASKET.get(user)).build());
    }

    public Optional<Receipt> checkout(long userId) {
        val user = BASKET.keySet().stream().filter(u -> u.getId() == userId).findFirst().orElseThrow(throwNoBasketException);
        List<ItemSubTotal> itemSubTotals = BASKET.get(user).keySet().stream().map(p -> {
            val deal = Optional.ofNullable(p.getDeal());
            var discount = BigDecimal.ONE;
            Class<?> dealClass = NoDiscount.class;
            if (deal.isPresent()) {
                try {
                    dealClass = Class.forName(DealEnum.getFullClassNameByClassName(deal.get().getStrategy()));
                    discount = deal.get().getDiscount();
                } catch (ClassNotFoundException ignored) { }
            }
            val handler = ((AbstractDealHandler) context.getBean(dealClass));
            handler.setDiscount(discount);
            return handler.handler(p, BASKET.get(user).get(p));
        }).collect(Collectors.toList());
        val firstCurrency = itemSubTotals.stream().map(i -> i.getProduct().getCurrency()).findFirst().orElseThrow();
        val total = itemSubTotals.stream().map(ItemSubTotal::getUnitSubTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        val items = BASKET.remove(user);
        if (items == null || (long) items.size() <= 0) {
            throw throwGeneralException.get();
        }
        return Optional.of(Receipt.builder().currency(firstCurrency).total(total).itemsBreakDown(itemSubTotals).build());
    }
}
