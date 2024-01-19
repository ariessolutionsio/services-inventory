package com.aries.inventory.availability.entity;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.Interleaved;
import com.google.cloud.spring.data.spanner.core.mapping.NotMapped;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "inventory_availability")
public class Inventory {
  @PrimaryKey
  @Column
  private String id;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  @Column(name = "sku_id")
  private String skuId;
  @Column(name = "node_id")
  private String nodeId;
  @Column(name = "product_key")
  private String productKey;
  @Column(name = "quantity_on_stock")
  private BigDecimal quantityOnStock;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Interleaved
  private List<InventoryReservation> reservations;
  @NotMapped
  private BigDecimal reservedQuantity;

  public BigDecimal getReservedQuantity(){
    BigDecimal reservedTotalQuantity = BigDecimal.ZERO;
    if(!CollectionUtils.isEmpty(reservations)){
      for(InventoryReservation reservation : reservations){
        reservedTotalQuantity = reservedTotalQuantity.add(reservation.getReservedQuantity());
      }
    }
    return reservedTotalQuantity;
  }

  public String getSkuId() {
    return skuId;
  }

  public void setSkuId(String skuId) {
    this.skuId = skuId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getProductKey() {
    return productKey;
  }

  public BigDecimal getQuantityOnStock() {
    return quantityOnStock;
  }

  public void setQuantityOnStock(BigDecimal quantityOnStock) {
    this.quantityOnStock = quantityOnStock;
  }

  public List<InventoryReservation> getReservations() {
    reservations = (Objects.isNull(reservations) || reservations.isEmpty()) ? new ArrayList<>() : reservations;
    return reservations;
  }

  public void setReservations(List<InventoryReservation> reservations) {
    this.reservations = reservations;
  }

  public void setProductKey(String productKey) {
    this.productKey = productKey;
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
}
