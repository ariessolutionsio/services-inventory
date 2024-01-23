package com.aries.inventory.availability.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuAvailabilityResponse {
    private List<SkuNodeResponse> availableItems;
}
