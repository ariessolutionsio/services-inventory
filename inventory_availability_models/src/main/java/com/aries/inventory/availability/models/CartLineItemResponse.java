package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartLineItemResponse {
    private String cartLineId;
    private String skuId;
    private BigDecimal available;
    private String message;
}
