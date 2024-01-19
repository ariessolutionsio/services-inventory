package com.aries.inventory.availability.repository;

import com.aries.inventory.availability.entity.InventoryReservation;
import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

public interface InventoryReservationRepository extends SpannerRepository<InventoryReservation, String> {
  InventoryReservation findByCartKeyAndLineId(String cartKey, String lineId);

  int deleteByCartKey(String cartKey);

  int deleteByLineId(String lineId);
}
