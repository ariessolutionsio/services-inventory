package com.aries.inventory.availability.service;

import com.aries.inventory.availability.entity.Inventory;
import com.aries.inventory.availability.entity.InventoryReservation;
import com.aries.inventory.availability.models.CartAvailabilityResponse;
import com.aries.inventory.availability.models.CartLineItemResponse;
import com.aries.inventory.availability.models.CartRequest;
import com.aries.inventory.availability.models.InventoryRequest;
import com.aries.inventory.availability.models.LineItemRequest;
import com.aries.inventory.availability.models.ProductForNodeResponse;
import com.aries.inventory.availability.models.ReservationLineItemResponse;
import com.aries.inventory.availability.models.ReservationResponse;
import com.aries.inventory.availability.models.SkuAvailabilityResponse;
import com.aries.inventory.availability.models.SkuLineResponse;
import com.aries.inventory.availability.models.SkuNodeResponse;
import com.aries.inventory.availability.repository.InventoryAvailabilityRepository;
import com.aries.inventory.availability.repository.InventoryReservationRepository;
import com.aries.inventory.availability.util.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AvailabilityService {
  
  public final static String DEFAULT = "default";
  private final InventoryAvailabilityRepository inventoryAvailabilityRepository;
  private final InventoryReservationRepository inventoryReservationRepository;

  public AvailabilityService(
      InventoryAvailabilityRepository inventoryAvailabilityRepository,
      InventoryReservationRepository inventoryReservationRepository) {
    this.inventoryAvailabilityRepository = inventoryAvailabilityRepository;
    this.inventoryReservationRepository = inventoryReservationRepository;
  }

  public Inventory addInventory(InventoryRequest inventoryRequest) {
    String node = StringUtils.isNotBlank(inventoryRequest.getNodeId()) ? inventoryRequest.getNodeId() : DEFAULT;
    Inventory inventoryItem = new Inventory();
    inventoryItem.setSkuId(inventoryRequest.getSkuId());
    inventoryItem.setNodeId(node);
    inventoryItem.setProductKey(inventoryRequest.getProductKey());
    inventoryItem.setQuantityOnStock(inventoryRequest.getQuantityOnStock());
    inventoryItem.setId(DBUtil.performHashing(inventoryRequest.getSkuId(), node));
    inventoryItem.setCreatedAt(LocalDateTime.now());inventoryItem.setUpdatedAt(LocalDateTime.now());
    inventoryItem = inventoryAvailabilityRepository.save(inventoryItem);
    return inventoryItem;
  }

  public ReservationResponse reserveAvailabilityForOrder(CartRequest cartRequest) {
    log.info("Removing existing reservation for cart if present no of items {}" ,removeReservationForExistingCart(cartRequest.getCartId()));
    List<InventoryReservation> inventoryReservationList = new ArrayList<>();
    List<String> notAvailableList = new ArrayList<>();
    List<ReservationLineItemResponse> reservationLineItemResponses = new ArrayList<>();

    for (LineItemRequest lineItemRequest : cartRequest.getItems()) {
      String node = StringUtils.isNotBlank(lineItemRequest.getNode()) ? lineItemRequest.getNode() : DEFAULT;
      String inventoryId = DBUtil.performHashing(lineItemRequest.getSkuId(), node);
      Optional<Inventory> inventoryOptional = inventoryAvailabilityRepository.findById(inventoryId);
      ReservationLineItemResponse response = new ReservationLineItemResponse();
      response.setLineId(lineItemRequest.getLineItemId());
      response.setSkuId(lineItemRequest.getSkuId());
      if(inventoryOptional.isPresent()){
        Inventory inventory = inventoryOptional.get();
        BigDecimal availableQuantity = calculateAvailability(inventory);
        if(availableQuantity.intValue() > (new BigDecimal(lineItemRequest.getQuantity())).intValue()){
          response.setReservedAmount(new BigDecimal(lineItemRequest.getQuantity()));
          InventoryReservation inventoryReservation = new InventoryReservation();
          inventoryReservation.setId(inventory.getId());
          inventoryReservation.setInventoryId(cartRequest.getCartId());
          inventoryReservation.setCartKey(cartRequest.getCartId());
          inventoryReservation.setLineId(lineItemRequest.getLineItemId());
          inventoryReservation.setUpdatedAt(LocalDateTime.now());
          inventoryReservation.setReservedQuantity(new BigDecimal(lineItemRequest.getQuantity()));
          inventoryReservation.setMessage("true");
          inventoryReservationList.add(inventoryReservation);
          inventory.getReservations().add(inventoryReservation);
          response.setMessage("Successfully reserved");
          availableQuantity = availableQuantity.subtract(new BigDecimal(lineItemRequest.getQuantity()));
          response.setQuantityOnStock(availableQuantity);
        } else {
          notAvailableList.add(lineItemRequest.getLineItemId());
          response.setReservedAmount(new BigDecimal("0"));
          response.setMessage("Not enough inventory");
        }
      } else {
        notAvailableList.add(lineItemRequest.getLineItemId());
        response.setQuantityOnStock(BigDecimal.ZERO);
        response.setReservedAmount(BigDecimal.ZERO);
        response.setMessage("Inventory not found");
      }
      reservationLineItemResponses.add(response);
    }
    if(notAvailableList.isEmpty() && !inventoryReservationList.isEmpty()){
      inventoryReservationRepository.saveAll(inventoryReservationList);
    }
    return new ReservationResponse(reservationLineItemResponses);
  }
  private int removeReservationForExistingCart(String cartKey){
    return inventoryReservationRepository.deleteByCartKey(cartKey);
  }

  public SkuAvailabilityResponse getAvailabilityOfSkuForNode(String sku, List<String> node) {
    List<SkuNodeResponse> availabilityInventories = new ArrayList<>();
    node.forEach(nodeId -> {
      String nodeVal = StringUtils.isNotBlank(nodeId) ? nodeId : DEFAULT;
      Optional<Inventory> inventoryOptional = inventoryAvailabilityRepository.findById(DBUtil.performHashing(sku, nodeVal));
      if(inventoryOptional.isPresent()){
        BigDecimal availableQuantity = calculateAvailability(inventoryOptional.get());
        availabilityInventories.add(new SkuNodeResponse(nodeId,sku,availableQuantity));
      }
    });
    return new SkuAvailabilityResponse(availabilityInventories);
  }

  public CartAvailabilityResponse getAvailabilityByCart(CartRequest cartRequest) {
    List<CartLineItemResponse> cartLineItemResponses = new ArrayList<>();
    for (LineItemRequest lineItemRequest : cartRequest.getItems()) {
      String node = StringUtils.isNotBlank(lineItemRequest.getNode()) ? lineItemRequest.getNode() : DEFAULT;
      String inventoryId = DBUtil.performHashing(lineItemRequest.getSkuId(), node);
      Optional<Inventory> inventoryOptional = inventoryAvailabilityRepository.findById(inventoryId);
      if (inventoryOptional.isPresent()) {
        Inventory inventory = inventoryOptional.get();
        CartLineItemResponse cartLineItemResponse = new CartLineItemResponse();
        cartLineItemResponse.setCartLineId(cartRequest.getCartId());
        BigDecimal availableStock = calculateAvailability(inventory);
        cartLineItemResponse.setAvailable(availableStock);
        cartLineItemResponse.setSkuId(inventory.getSkuId());
        if (availableStock.intValue() > (new BigDecimal(lineItemRequest.getQuantity())).intValue()) {
          cartLineItemResponse.setMessage(Boolean.TRUE.toString());
        } else {
          cartLineItemResponse.setMessage(Boolean.FALSE.toString());
        }
        cartLineItemResponses.add(cartLineItemResponse);
      }
    }
    return new CartAvailabilityResponse(cartLineItemResponses);
  }
  private BigDecimal calculateAvailability(Inventory inventory){
    return inventory.getQuantityOnStock().subtract(inventory.getReservedQuantity());
  }

  public ProductForNodeResponse getAvailabilityByProductForNode(String productKey, String node) {
    List<SkuLineResponse> skuResponses = new ArrayList<>();
    ProductForNodeResponse response = new ProductForNodeResponse();
    response.setNodeId(node); response.setProductKey(productKey);
    node = StringUtils.isNotBlank(node) ? node : DEFAULT;
    List<Inventory> inventories = inventoryAvailabilityRepository.findByProductKeyAndNodeIdOrderBySkuId(productKey,node);
    inventories.forEach(inventory -> {
      BigDecimal availableQuantity = calculateAvailability(inventory);
      skuResponses.add(new SkuLineResponse(inventory.getSkuId(), availableQuantity));
    });
    response.setAvailableItems(skuResponses);
    return response;
  }

  public String removeReservationsForCartItems(String cartId, List<String> lines) {
    String response = "";
    if (CollectionUtils.isEmpty(lines)) {
      removeReservationForExistingCart(cartId);
      response = "Removed all reservations for Cart :" + cartId;
    } else {
      boolean exceptionOccurred = Boolean.FALSE;
      for (String line : lines) {
        if (Objects.isNull(inventoryReservationRepository.findByCartKeyAndLineId(cartId, line))) {
          response = "Reservation not found for cartId : ".concat(cartId).concat(" lineId : " + line);
          exceptionOccurred = Boolean.TRUE;
          break;
        }
        inventoryReservationRepository.deleteByLineId(line);
      }
      if (!exceptionOccurred) {
        response = "Deleted successfully";
      }
    }
    return response;
  }

  public void updateInventoryForNodeAndRemoveAllReservations(String sku, String nodeId, String amount) {
    BigDecimal newStockAmount = BigDecimal.ZERO;
    if(StringUtils.isNotBlank(amount)){
      newStockAmount = new BigDecimal(amount);
    }
    nodeId = StringUtils.isNotBlank(nodeId) ? nodeId : DEFAULT;
    String id = DBUtil.performHashing(sku, nodeId);
    Optional<Inventory> inventoryOptional = inventoryAvailabilityRepository.findById(id);
    if(inventoryOptional.isPresent()){
      Inventory inventory = inventoryOptional.get();
      inventory.setQuantityOnStock(newStockAmount);
      inventory.setUpdatedAt(LocalDateTime.now());
      List<InventoryReservation> reservations = inventory.getReservations();
      inventoryReservationRepository.deleteAll(reservations);
      inventory.setReservations(new ArrayList<>());
      inventory = inventoryAvailabilityRepository.save(inventory);
      log.info("inventory sku {}, node {}, size {}",inventory.getSkuId(), inventory.getNodeId(), inventory.getReservations().size());
    }

  }
}
