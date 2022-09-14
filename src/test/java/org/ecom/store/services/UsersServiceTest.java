package org.ecom.store.services;

import lombok.val;
import org.ecom.store.deals.DealEnum;
import org.ecom.store.deals.NoDiscount;
import org.ecom.store.deals.SecondItemDiscount;
import org.ecom.store.exceptions.NotEnoughQuantityInBasketException;
import org.ecom.store.exceptions.NotEnoughStockLevelException;
import org.ecom.store.exceptions.ProductNotFoundException;
import org.ecom.store.models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Stream;

import static org.ecom.store.data.Store.STOCK;
import static org.ecom.store.exceptions.ExceptionsEnum.*;
import static org.ecom.store.exceptions.ExceptionsEnum.NOT_ENOUGH_QUANTITY_FOR_REMOVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersServiceTest {
    private ApplicationContext context = mock(ApplicationContext.class);
    private UsersService usersService = new UsersService(context);
    private static StoreService storeService = new StoreService();

    @BeforeAll
    static void beforeClass() {
        STOCK.clear();
    }

    static Stream<Arguments> addItemArgumentsProvider(){
        val user1 = User.builder().id(1L).name("User1").currency("HKD").build();
        val user1Item1 = UserItem.builder().user(user1).productId(1L).quantity(BigInteger.ONE).build();
        val product1Stored = Product.builder()
                .id(2L)
                .serialNumber("a00000000000000015")
                .name("iPhone 14 Pro")
                .brandName("Apple")
                .description("Apple iPhone 14 Pro")
                .currency("HKD")
                .price(BigDecimal.valueOf(8499.00))
                .build();
        val storeItem2 = Item.builder().product(product1Stored).quantity(BigInteger.TEN).build();
        if(storeService.create(storeItem2).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val user1Item2 = UserItem.builder().user(user1).productId(2L).quantity(BigInteger.ONE).build();
        val product2Stored = Product.builder()
                .id(3L)
                .serialNumber("a00000000000000013")
                .name("iPhone 13")
                .brandName("Apple")
                .description("Apple iPhone 13")
                .currency("USD")
                .price(BigDecimal.valueOf(499.00))
                .build();
        val storeItem3 = Item.builder().product(product2Stored).quantity(BigInteger.TEN).build();
        if (storeService.create(storeItem3).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val user1Item2OverQuantity = UserItem.builder().user(user1).productId(2L).quantity(BigInteger.valueOf(11L)).build();
        return Stream.of(
                Arguments.of(user1Item1, new ProductNotFoundException(PRODUCT_NOT_FOUND.getCode(), PRODUCT_NOT_FOUND.getMessage())),
                Arguments.of(user1Item2, null),
                Arguments.of(user1Item2OverQuantity, new NotEnoughStockLevelException(NOT_ENOUGH_QUANTITY_FOR_SELL.getCode(), NOT_ENOUGH_QUANTITY_FOR_SELL.getMessage()))
        );
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("addItemArgumentsProvider")
    void addItemTest(UserItem userItem, Exception e) {
        if (e != null) {
            val thrown = assertThrows(Exception.class, () -> usersService.addItem(userItem));
            assertEquals(e.getClass(), thrown.getClass());
            assertEquals(e.getMessage(), thrown.getMessage());
        } else {
            val basket = usersService.addItem(userItem);
            if (basket.isPresent()) {
                assertTrue(basket.get().getItems().keySet().stream().allMatch(p -> p.getId() == userItem.getProductId()));
            } else {
                throw new AssertionError("Cannot save item in user basket!");
            }
        }
    }

    static Stream<Arguments> removeItemArgumentsProvider(){
        val user1 = User.builder().id(2L).name("User2").currency("HKD").build();
        val product1Stored = Product.builder()
                .id(10L)
                .serialNumber("m00000000000000001")
                .name("Redmi 14")
                .brandName("Mi")
                .description("Mi Redmi 14")
                .currency("HKD")
                .price(BigDecimal.valueOf(2499.00))
                .build();
        val storeItem2 = Item.builder().product(product1Stored).quantity(BigInteger.TEN).build();
        if(storeService.create(storeItem2).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val user1Item1 = UserItem.builder().user(user1).productId(10L).quantity(BigInteger.TWO).build();
        return Stream.of(
                Arguments.of(user1Item1, user1.getId(), product1Stored.getId(), BigInteger.ONE, null),
                Arguments.of(user1Item1, user1.getId(), product1Stored.getId(), BigInteger.ONE, null),
                Arguments.of(user1Item1, user1.getId(), product1Stored.getId(), BigInteger.ONE,
                        new NotEnoughQuantityInBasketException(NOT_ENOUGH_QUANTITY_FOR_REMOVE.getCode(), NOT_ENOUGH_QUANTITY_FOR_REMOVE.getMessage()))
        );
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("removeItemArgumentsProvider")
    void removeItemTest(UserItem userItem, long userId, long productId, BigInteger quantity) {
        val basketAdded = usersService.addItem(userItem);
        if (basketAdded.isPresent()) {
            val basketDeleted = usersService.removeItem(userId, productId, quantity);
            if (basketDeleted.isPresent()) {
                assertEquals(basketDeleted.get().getUser().getId(), userId);
                val remainQuantity = basketAdded.get().getItems().entrySet().stream().findFirst()
                        .orElseThrow().getValue().subtract(quantity);
                if (remainQuantity.compareTo(BigInteger.ZERO) >= 0) {
                    assertEquals(basketDeleted.get().getItems().entrySet().stream().findFirst().orElseThrow().getKey().getId(), productId);
                } else {
                    assertTrue(basketDeleted.get().getItems().isEmpty());
                }
            } else {
                throw new AssertionError("Cannot delete item in user basket!");
            }
        } else {
            throw new AssertionError("Cannot add item in user basket!");
        }
    }

    static Stream<Arguments> checkoutArgumentsProvider() {
        val user = User.builder().id(3L).name("User3").currency("HKD").build();
        val productWithoutDeal = Product.builder()
                .id(20L)
                .serialNumber("s00000000000000015")
                .name("S21 Ultra")
                .brandName("Samsung")
                .description("Samsung S21 Ultra")
                .currency("HKD")
                .price(BigDecimal.valueOf(8499.00))
                .build();
        val storeItemWithoutDeal = Item.builder().product(productWithoutDeal).quantity(BigInteger.TEN).build();
        if (storeService.create(storeItemWithoutDeal).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val userItemWithoutDeal = UserItem.builder().user(user).productId(20L).quantity(BigInteger.TWO).build();
        val deal = Deal.builder().strategy("SecondItemDiscount").discount(BigDecimal.valueOf(0.5D)).build();
        val productWithDeal = Product.builder()
                .id(21L)
                .serialNumber("s00000000000000013")
                .name("S20 Ultra")
                .brandName("Samsung")
                .description("Samsung S20 Ultra")
                .currency("HKD")
                .price(BigDecimal.valueOf(6400.00))
                .deal(deal)
                .build();
        val storeItemWithDeal = Item.builder().product(productWithDeal).quantity(BigInteger.TEN).build();
        if (storeService.create(storeItemWithDeal).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val userItemWithDeal = UserItem.builder().user(user).productId(21L).quantity(BigInteger.TWO).build();
        return Stream.of(
                Arguments.of(userItemWithoutDeal, productWithoutDeal, user.getId(), BigDecimal.valueOf(16998.00)),
                Arguments.of(userItemWithDeal, productWithDeal, user.getId(), BigDecimal.valueOf(9600.00))
        );
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("checkoutArgumentsProvider")
    void checkoutTest(UserItem userItem, Product product, long userId, BigDecimal expectedTotal) {
        val strategyExisted = product.getDeal() == null ||
                product.getDeal().getStrategy().equals(DealEnum.NO_DISCOUNT.getClassName());
        if (strategyExisted) {
            when(context.getBean(NoDiscount.class))
                    .thenReturn(new NoDiscount());
        } else {
            when(context.getBean(SecondItemDiscount.class))
                    .thenReturn(new SecondItemDiscount());
        }
        val basketAdded = usersService.addItem(userItem);
        if (basketAdded.isPresent()) {
            val receipt = usersService.checkout(userId);
            if (receipt.isPresent()) {
                assertTrue(receipt.get().getTotal()
                        .compareTo(expectedTotal) == 0);
            } else {
                throw new AssertionError("Cannot calculate receipt of user basket!");
            }
        } else {
            throw new AssertionError("Cannot add item in user basket!");
        }
    }
}
