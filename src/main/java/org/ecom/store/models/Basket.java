package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
@Builder
public class Basket {
    User user;
    ConcurrentHashMap<Product, BigInteger> items;
}
