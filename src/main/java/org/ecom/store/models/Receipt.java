package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Receipt {
    List<ItemSubTotal> itemsBreakDown;
    String currency;
    BigDecimal total;
}
