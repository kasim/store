package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class Item {
    private Product product;
    private BigInteger quantity;
}
