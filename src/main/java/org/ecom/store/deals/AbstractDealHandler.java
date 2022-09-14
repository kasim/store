package org.ecom.store.deals;

import lombok.Setter;

import java.math.BigDecimal;

@Setter
public abstract class AbstractDealHandler implements ItemDealHandler{
    BigDecimal discount;
}
