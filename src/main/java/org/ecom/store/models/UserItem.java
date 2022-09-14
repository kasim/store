package org.ecom.store.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigInteger;

@Data
@Getter
@Builder
public class UserItem {
    User user;
    long productId;
    BigInteger quantity;
}
