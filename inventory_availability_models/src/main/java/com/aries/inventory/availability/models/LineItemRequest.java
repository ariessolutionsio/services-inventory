package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemRequest {
    private String lineItemId;
    @NonNull
    private String skuId;
    private String node;
    @NonNull
    private String quantity;
}
