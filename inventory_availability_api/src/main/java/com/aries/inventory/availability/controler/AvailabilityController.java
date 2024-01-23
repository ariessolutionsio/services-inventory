package com.aries.inventory.availability.controler;

import com.aries.inventory.availability.entity.Inventory;
import com.aries.inventory.availability.models.CartAvailabilityResponse;
import com.aries.inventory.availability.models.CartRequest;
import com.aries.inventory.availability.models.InventoryRequest;
import com.aries.inventory.availability.models.ProductForNodeResponse;
import com.aries.inventory.availability.models.ReservationResponse;
import com.aries.inventory.availability.models.SkuAvailabilityResponse;
import com.aries.inventory.availability.service.AvailabilityService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/availability")
public class AvailabilityController {
  private final AvailabilityService availabilityService;

  public AvailabilityController(AvailabilityService availabilityService) {
    this.availabilityService = availabilityService;
  }

  /**
   * Allows the UI to check all items in the cart for availability (typically upon entering checkout
   * and possibly right before order submit).
   *
   * @return ResponseEntity
   */
  @GetMapping("/cart/{cartId}")
  public ResponseEntity<CartAvailabilityResponse> getAvailabilityByCartId(
          @PathVariable("cartId") String cartId, @RequestBody CartRequest cartRequest) {
    cartRequest.setCartId(cartId);
    CartAvailabilityResponse response = availabilityService.getAvailabilityByCart(cartRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * When a cart is converted to an order, the items in the order will need to be reserved.
   *
   * @return ResponseEntity
   */
  @PostMapping("/order/{cartId}")
  public ResponseEntity<ReservationResponse> reserveAvailabilityForOrder(
      @PathVariable("cartId") String cartId, @RequestBody CartRequest cartRequest) {
    cartRequest.setCartId(cartId);
    return new ResponseEntity<>(availabilityService.reserveAvailabilityForOrder(cartRequest), HttpStatus.OK);
  }

  @PostMapping("/addInventory")
  public ResponseEntity<Inventory> addInventory(@RequestBody InventoryRequest inventoryRequest) {
    Inventory inventory = availabilityService.addInventory(inventoryRequest);
    return new ResponseEntity<>(inventory, HttpStatus.OK);
  }

  /**
   * Check all items in the cart against available inventory at that node
   *
   * @return ResponseEntity
   */
  @GetMapping("/cart/{cartId}/node/{nodeId}")
  //Same as by cartId with CartRequest with node set
  public ResponseEntity<Object> getAvailabilityByCartIdOnNode() {
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  /**
   * Passing the sku, retrieves the available inventory of a product from a node list
   * Usage: http://localhost:8080/v1/availability/{skuId}?node=1,,2
   * @return ResponseEntity
   */
  @GetMapping("/{skuId}")
  public ResponseEntity<SkuAvailabilityResponse> getAvailabilityBySkuIdForNodes(@PathVariable String skuId, @NonNull @RequestParam List<String> node) {
     return new ResponseEntity<>(availabilityService.getAvailabilityOfSkuForNode(skuId, node), HttpStatus.OK);
  }

  /*
   * Returns inventory of all variants (SKU) of a given product for a given node
   * usage : http://localhost:8080/v1/availability/product/1?node=1
   */
  @GetMapping("/product/{productKey}")
  public ResponseEntity<ProductForNodeResponse> getAvailabilityByProductForNode(@PathVariable String productKey, @RequestParam String node) {
    return new ResponseEntity<>(availabilityService.getAvailabilityByProductForNode(productKey, node), HttpStatus.OK);
  }
  /**
   * Removes the reservations in the DB and updates the available inventory to match downstream
   * systems.
   */
  @PutMapping("/{sku}/node/{nodeId}/quantity/{amount}")
  public ResponseEntity<Object> updateInventoryForNodeAndRemoveAllReservations(
      @PathVariable String sku, @PathVariable String nodeId, @PathVariable String amount) {
    availabilityService.updateInventoryForNodeAndRemoveAllReservations(sku, nodeId, amount);
    return new ResponseEntity<>("Stock updated Successfully", HttpStatus.NO_CONTENT);
  }
  /**
   * Delete the reservation of lines on nodes and make the inventory available again. If lines not
   * passed then remove all reservation of cart lines.
   * usage : http://localhost:8080/v1/availability/reservation/{cartId}?lines={lineid1},{lineid2}
   */
  @DeleteMapping("/reservation/{cartId}")
  public ResponseEntity<String> removeReservationsForCartItems(@PathVariable String cartId, @Nullable @RequestParam List<String> lines) {
    String response = availabilityService.removeReservationsForCartItems(cartId, lines);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
