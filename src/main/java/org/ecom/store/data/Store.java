package org.ecom.store.data;

import org.ecom.store.models.Product;
import org.ecom.store.models.User;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    public static final Map<Product, BigInteger> STOCK = new ConcurrentHashMap<>();
    public static final Map<User, ConcurrentHashMap<Product, BigInteger>> BASKET = new ConcurrentHashMap<>();
}
