package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
public class ItemSubTotal {
    Product product;
    BigInteger quantity;
    BigDecimal unitSubTotal;
}
