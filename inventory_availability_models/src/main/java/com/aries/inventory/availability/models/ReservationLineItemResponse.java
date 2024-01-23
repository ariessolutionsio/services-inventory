package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLineItemResponse {
    private String lineId;
    private String skuId;
    private BigDecimal quantityOnStock;
    private BigDecimal reservedAmount;
    private String message;
}
