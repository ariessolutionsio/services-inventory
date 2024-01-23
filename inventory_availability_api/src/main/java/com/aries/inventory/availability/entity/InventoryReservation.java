package com.aries.inventory.availability.entity;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.NotMapped;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "inventory_reservation")
public class InventoryReservation {
  @PrimaryKey(keyOrder = 1)
  private String id;
  public String getId() {
    return id;
  }
  @PrimaryKey(keyOrder = 2)
  @Column(name = "inventory_id")
  private String inventoryId;
  public void setId(String id) {
    this.id = id;
  }
  @Column(name = "cart_key")
  private String cartKey;

  @Column(name = "line_id")
  private String lineId;
  @Column(name = "reserved_quantity")
  private BigDecimal reservedQuantity;
  @Column
  private String message;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @NotMapped
  private Inventory inventoryReference;


  public String getInventoryId() {
    return inventoryId;
  }

  public void setInventoryId(String inventoryId) {
    this.inventoryId = inventoryId;
  }

  public String getCartKey() {
    return cartKey;
  }

  public void setCartKey(String cartKey) {
    this.cartKey = cartKey;
  }

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public BigDecimal getReservedQuantity() {
    return reservedQuantity;
  }

  public void setReservedQuantity(BigDecimal reservedQuantity) {
    this.reservedQuantity = reservedQuantity;
  }

  public String getMessage() {
    return message;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Inventory getInventoryReference() {
    return inventoryReference;
  }

  public void setInventoryReference(Inventory inventoryReference) {
    this.inventoryReference = inventoryReference;
  }

}
