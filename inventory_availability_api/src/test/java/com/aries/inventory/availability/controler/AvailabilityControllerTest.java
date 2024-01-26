package com.aries.inventory.availability.controler;

import com.aries.inventory.availability.entity.Inventory;
import com.aries.inventory.availability.entity.InventoryReservation;
import com.aries.inventory.availability.models.CartAvailabilityResponse;
import com.aries.inventory.availability.models.CartRequest;
import com.aries.inventory.availability.models.InventoryRequest;
import com.aries.inventory.availability.models.LineItemRequest;
import com.aries.inventory.availability.models.ProductForNodeResponse;
import com.aries.inventory.availability.models.ReservationResponse;
import com.aries.inventory.availability.models.SkuAvailabilityResponse;
import com.aries.inventory.availability.repository.InventoryAvailabilityRepository;
import com.aries.inventory.availability.repository.InventoryReservationRepository;
import com.aries.inventory.availability.service.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AvailabilityControllerTest {
    @Mock
    InventoryAvailabilityRepository inventoryAvailabilityRepository;
    @Mock
    InventoryReservationRepository inventoryReservationRepository;
    AvailabilityService availabilityService;
    AvailabilityController availabilityController;
    public AvailabilityControllerTest(){
    }
    @BeforeEach
    void setup(){
        this.availabilityService = new AvailabilityService(inventoryAvailabilityRepository,inventoryReservationRepository);
        this.availabilityController = new AvailabilityController(availabilityService);

        Inventory inventory = new Inventory();
        inventory.setId("fe7fb8326d3f14d9cbd7292c8671a26a");
        inventory.setSkuId("3332");
        inventory.setNodeId("222");
        inventory.setProductKey("121212");
        inventory.setQuantityOnStock(new BigDecimal(100));
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());

        InventoryReservation reservation = new InventoryReservation();
        reservation.setId("aaaabbbb");
        reservation.setInventoryId("fe7fb8326d3f14d9cbd7292c8671a26a");
        reservation.setCartKey("aaaabbbb");
        reservation.setLineId("111aa111");
        reservation.setReservedQuantity(new BigDecimal(2));
        reservation.setMessage("True");
        List<InventoryReservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        inventory.setReservations(reservationList);
        Optional<Inventory> inventoryOptional = Optional.ofNullable(inventory);
        when(inventoryAvailabilityRepository.findById(anyString())).thenReturn(inventoryOptional);
        when(inventoryAvailabilityRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryAvailabilityRepository.findByProductKeyAndNodeIdOrderBySkuId(anyString(), anyString())).thenReturn(Arrays.asList(inventory));

        Optional<InventoryReservation> inventoryResOptional = Optional.ofNullable(reservation);
        when(inventoryReservationRepository.findById(anyString())).thenReturn(inventoryResOptional);
        when(inventoryReservationRepository.save(any(InventoryReservation.class))).thenReturn(reservation);
        when(inventoryReservationRepository.findByCartKeyAndLineId(anyString(), anyString())).thenReturn(reservation);
        when(inventoryReservationRepository.deleteByCartKey(anyString())).thenReturn(1);
        when(inventoryReservationRepository.deleteByLineId(anyString())).thenReturn(1);

    }

  @Test
  void addInventoryTest() {
    InventoryRequest inventoryRequest = new InventoryRequest();
    inventoryRequest.setSkuId("3332");
    inventoryRequest.setNodeId("222");
    inventoryRequest.setProductKey("121212");
    inventoryRequest.setQuantityOnStock(new BigDecimal(100));
    ResponseEntity<Inventory> response = availabilityController.addInventory(inventoryRequest);
    assertEquals(response.getBody().getId(), "fe7fb8326d3f14d9cbd7292c8671a26a");
  }
    @Test
    void getAvailabilityByCartIdTest() {
        LineItemRequest lineItemRequest = new LineItemRequest();
        lineItemRequest.setLineItemId("111aa111");lineItemRequest.setSkuId("3332");lineItemRequest.setNode("222");lineItemRequest.setQuantity("1");
        CartRequest cartRequest = new CartRequest(); cartRequest.setCartId("aaaabbbb");cartRequest.setItems(Arrays.asList(lineItemRequest));
        ResponseEntity<CartAvailabilityResponse> response = availabilityController.getAvailabilityByCartId("aaaabbbb", cartRequest);
        assertEquals(response.getBody().getAvailableItems().size(), 1);
    }
    @Test
    void getAvailabilityByProductForNodeTest() {
        ResponseEntity<ProductForNodeResponse> response = availabilityController.getAvailabilityByProductForNode("121212", "222");
        assertEquals(response.getBody().getAvailableItems().get(0).getQuantity().intValue(), 98);
    }
    @Test
    void getAvailabilityBySkuIdForNodesTest() {
        ResponseEntity<SkuAvailabilityResponse> response = availabilityController.getAvailabilityBySkuIdForNodes("3332", Arrays.asList("222"));
        assertEquals(response.getBody().getAvailableItems().get(0).getQuantity().intValue(), 98);
    }
    @Test
    void reserveAvailabilityForOrderTest() {
        LineItemRequest lineItemRequest = new LineItemRequest();
        lineItemRequest.setLineItemId("111aa111");lineItemRequest.setSkuId("3332");lineItemRequest.setNode("222");lineItemRequest.setQuantity("1");
        CartRequest cartRequest = new CartRequest(); cartRequest.setCartId("aaaabbbb");cartRequest.setItems(Arrays.asList(lineItemRequest));
        ResponseEntity<ReservationResponse> response = availabilityController.reserveAvailabilityForOrder("aaaabbbb", cartRequest);
        assertEquals(response.getBody().getReservedItems().get(0).getQuantityOnStock().intValue(), 97);
    }
    @Test
    void removeReservationsForCartItemsTest() {
        ResponseEntity<String> response = availabilityController.removeReservationsForCartItems("aaaabbbb", Arrays.asList("3332"));
        assertEquals(response.getBody(), "Deleted successfully");
    }
    @Test
    void updateInventoryForNodeAndRemoveAllReservationsTest() {
        ResponseEntity<Object> response = availabilityController.updateInventoryForNodeAndRemoveAllReservations("3332", "222", "1");
        assertEquals(response.getBody(), "Stock updated Successfully");
    }

}
