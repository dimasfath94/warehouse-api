package com.shop.warehouse.controller;

import com.shop.warehouse.dto.ItemRequest;
import com.shop.warehouse.dto.StockDeductionRequest;
import com.shop.warehouse.entity.Item;
import com.shop.warehouse.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Warehouse Inventory Management", description = "Endpoints for managing items, variants, and stock levels")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "Create a new item with its variants")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest request) {
        Item createdItem = warehouseService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping
    @Operation(summary = "Get all items including their variants")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(warehouseService.getAllItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item by its UUID")
    public ResponseEntity<Item> getItemById(@PathVariable UUID id) {
        return ResponseEntity.ok(warehouseService.getItemById(id));
    }

    // =========================================================================
    // PENAMBAHAN: Endpoint Update Item (Melengkapi Fitur CRUD)
    // =========================================================================
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item and its variants by UUID")
    public ResponseEntity<Item> updateItem(@PathVariable UUID id, @Valid @RequestBody ItemRequest request) {
        Item updatedItem = warehouseService.updateItem(id, request);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item and all its variants")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        warehouseService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deduct-stock")
    @Operation(summary = "Deduct stock for multiple items (Simulate order/sale with race-condition protection)")
    public ResponseEntity<Void> deductStock(@Valid @RequestBody List<StockDeductionRequest> deductions) {
        warehouseService.deductStock(deductions);
        return ResponseEntity.ok().build();
    }
}