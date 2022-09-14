package org.ecom.store.services;

import lombok.val;
import org.ecom.store.exceptions.*;
import org.ecom.store.models.Deal;
import org.ecom.store.models.Item;
import org.ecom.store.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Stream;

import static org.ecom.store.data.Store.STOCK;
import static org.ecom.store.exceptions.ExceptionsEnum.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreServiceTest {
    StoreService storeService = new StoreService();

    static Stream<Arguments> createProductParametersProvider() {
        return Stream.of(
                Arguments.of(1L, "a00000000000000014", "iPhone 14 Pro",
                        "Apple", "Apple iPhone 14 Pro", "HKD",
                        BigDecimal.valueOf(8499.00), BigInteger.TEN,
                        null, null,
                        null),
                Arguments.of(2L, "a00000000000000015", "iPhone 14 Pro Max",
                        "Apple", "Apple iPhone 14 Pro Max", "HKD",
                        BigDecimal.valueOf(10499.00), BigInteger.TEN,
                        null, null,
                        null),
                Arguments.of(1L, "a00000000000000015", "iPhone 14 Pro Max",
                        "Apple", "Apple iPhone 14 Pro Max", "HKD",
                        BigDecimal.valueOf(10499.00), BigInteger.TEN,
                        null, null,
                        new DuplicatedProductIdException(DUPLICATED_PRODUCT_ID.getCode(), DUPLICATED_PRODUCT_ID.getMessage())),
                Arguments.of(3L, "a00000000000000013", "iPhone 13 Pro",
                        "Apple", "Apple iPhone 13 Pro", "HKD",
                        BigDecimal.valueOf(7499.00), BigInteger.TEN,
                        "WrongDiscount", BigDecimal.valueOf(0.5D),
                        new NoDiscountStrategyException(NO_MATCHED_STRATEGY_NAME.getCode(), NO_MATCHED_STRATEGY_NAME.getMessage())),
                Arguments.of(3L, "a00000000000000013", "iPhone 13 Pro",
                        "Apple", "Apple iPhone 13 Pro", "HKD",
                        BigDecimal.valueOf(7499.00), BigInteger.TEN,
                        "SecondItemDiscount", null,
                        new NoDiscountRateSpecifiedException(NO_DISCOUNT_RATE.getCode(), NO_DISCOUNT_RATE.getMessage())),
                Arguments.of(3L, "a00000000000000013", "iPhone 13 Pro",
                        "Apple", "Apple iPhone 13 Pro", "HKD",
                        BigDecimal.valueOf(7499.00), BigInteger.TEN,
                        "SecondItemDiscount", BigDecimal.valueOf(-0.1D),
                        new WrongDiscountRateException(WRONG_DISCOUNT_RATE.getCode(), WRONG_DISCOUNT_RATE.getMessage())),
                Arguments.of(3L, "a00000000000000013", "iPhone 13 Pro",
                        "Apple", "Apple iPhone 13 Pro", "HKD",
                        BigDecimal.valueOf(7499.00), BigInteger.TEN,
                        "SecondItemDiscount", BigDecimal.valueOf(1.1D),
                        new WrongDiscountRateException(WRONG_DISCOUNT_RATE.getCode(), WRONG_DISCOUNT_RATE.getMessage()))
        );
    }

    @BeforeAll
    static void beforeClass() {
        STOCK.clear();
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("createProductParametersProvider")
    void createProductTest(long id, String serialNumber, String name,
                           String brandName, String description,
                           String currency, BigDecimal price, BigInteger quantity,
                           String strategy, BigDecimal discount,
                           Exception expected) {
        Deal deal = null;
        if(strategy != null || discount != null) {
             deal = Deal.builder().strategy(strategy).discount(discount).build();
        }
        val product = Product.builder()
                .id(id)
                .serialNumber(serialNumber)
                .name(name)
                .brandName(brandName)
                .description(description)
                .currency(currency)
                .price(price)
                .deal(deal)
                .build();
        val item = Item.builder().product(product).quantity(quantity).build();
        if(expected == null) {
            assertEquals(Optional.of(item), storeService.create(item));
            assertTrue(STOCK.containsKey(product));
            assertTrue(BigInteger.TEN.compareTo(STOCK.get(product)) == 0);
        } else {
            val thrown = assertThrows(expected.getClass(), () -> storeService.create(item));
            assertEquals(expected.getCause(), thrown.getCause());
            assertEquals(expected.getMessage(), thrown.getMessage());
        }
    }

    static Stream<Arguments> deleteProductParametersProvider() {
        val product = Product.builder()
                .id(4L)
                .serialNumber("a00000000000000012")
                .name("iPhone 12")
                .brandName("Apple")
                .description("Apple iPhone 12")
                .currency("HKD")
                .price(BigDecimal.valueOf(6499.00))
                .build();
        val item = Item.builder().product(product).quantity(BigInteger.TEN).build();
        return Stream.of(
                Arguments.of(null, 4L, new ProductNotFoundException(PRODUCT_NOT_FOUND.getCode(), PRODUCT_NOT_FOUND.getMessage())),
                Arguments.of(item, 4L, null)
        );
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("deleteProductParametersProvider")
    void deleteProductTest(Item item, long id, Exception expected){
        if (item != null) {
            val created = storeService.create(item);
            val product = storeService.delete(id);
            if(created.isPresent() && product.isPresent()) {
                assertEquals(created.get().getProduct(), product.get());
            } else {
                throw new AssertionError("Cannot create item or delete product");
            }
        } else {
            val thrown = assertThrows(expected.getClass(), () -> storeService.delete(id));
            assertEquals(expected.getCause(), thrown.getCause());
            assertEquals(expected.getMessage(), thrown.getMessage());
        }
    }
}
