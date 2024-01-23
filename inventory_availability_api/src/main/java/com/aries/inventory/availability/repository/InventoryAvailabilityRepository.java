package com.aries.inventory.availability.repository;

import com.aries.inventory.availability.entity.Inventory;
import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

import java.util.List;

public interface InventoryAvailabilityRepository extends SpannerRepository<Inventory, String> {
  List<Inventory> findByProductKeyAndNodeIdOrderBySkuId(String productKey, String node);
}
