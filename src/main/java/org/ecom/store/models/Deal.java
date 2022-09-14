package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Deal {
    String strategy;
    BigDecimal discount;
}
