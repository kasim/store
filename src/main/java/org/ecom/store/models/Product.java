package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {
    private long id;
    private String serialNumber;
    private String name;
    private String brandName;
    private String description;
    private String currency;
    private BigDecimal price;
    private Deal deal;
}
