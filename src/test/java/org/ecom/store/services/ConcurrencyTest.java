package org.ecom.store.services;

import lombok.val;
import org.ecom.store.models.Item;
import org.ecom.store.models.Product;
import org.ecom.store.models.User;
import org.ecom.store.models.UserItem;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;

import static org.ecom.store.data.Store.STOCK;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ConcurrencyTest {
    private ApplicationContext context = mock(ApplicationContext.class);
    private UsersService usersService = new UsersService(context);
    private StoreService storeService = new StoreService();

    @Test
    void twoUsersBuyOutAProduct() throws InterruptedException {
        val numberOfUser = 2;
        final CountDownLatch latch = new CountDownLatch(numberOfUser);
        val user1 = User.builder().id(1L).name("User1").currency("HKD").build();
        val user2 = User.builder().id(2L).name("User2").currency("HKD").build();
        val productId = 33L;
        val product = Product.builder()
                .id(productId)
                .serialNumber("a00000000000000016")
                .name("iPhone 14 Pro Max")
                .brandName("Apple")
                .description("Apple iPhone 14 Pro Max")
                .currency("HKD")
                .price(BigDecimal.valueOf(12499.00))
                .build();
        val stockLevel = BigInteger.TEN;
        val item = Item.builder().product(product).quantity(stockLevel).build();
        if(storeService.create(item).isEmpty()) {
            throw new AssertionError("Cannot create store item!");
        }
        val user1Item = UserItem.builder().user(user1).productId(productId).quantity(BigInteger.ONE).build();
        val user2Item = UserItem.builder().user(user2).productId(productId).quantity(BigInteger.ONE).build();
        Thread user1Thread = new Thread(() -> {
            try {
                for(int i = 0; i < stockLevel.intValue(); ++i) {
                    usersService.addItem(user1Item);
                }
            } catch (Exception ignored) {}
            finally {
                latch.countDown();
            }
        });
        Thread user2Thread = new Thread(() -> {
            try {
                for (int i = 0; i < stockLevel.intValue(); ++i) {
                    usersService.addItem(user2Item);
                }
            } catch (Exception ignored) {}
            finally {
                latch.countDown();
            }
        });

        user1Thread.start();
        user2Thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(STOCK.get(product).compareTo(BigInteger.ZERO) == 0);
    }
}
