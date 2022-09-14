package org.ecom.store.deals;

import org.ecom.store.models.ItemSubTotal;
import org.ecom.store.models.Product;

import java.math.BigInteger;

public interface ItemDealHandler {
    ItemSubTotal handler(Product product, BigInteger quantity);
}
