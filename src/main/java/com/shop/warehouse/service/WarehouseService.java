package com.shop.warehouse.service;

import com.shop.warehouse.dto.ItemRequest;
import com.shop.warehouse.dto.StockDeductionRequest;
import com.shop.warehouse.entity.Item;
import com.shop.warehouse.entity.ItemVariant;
import com.shop.warehouse.exception.InsufficientStockException;
import com.shop.warehouse.exception.ResourceNotFoundException;
import com.shop.warehouse.repository.ItemRepository;
import com.shop.warehouse.repository.ItemVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final ItemRepository itemRepository;
    private final ItemVariantRepository itemVariantRepository;

    @Transactional
    public Item createItem(ItemRequest request) {
        Item item = Item.builder()
                .name(request.name())
                .description(request.description())
                .build();

        request.variants().forEach(v -> {
            ItemVariant variant = ItemVariant.builder()
                    .sku(v.sku())
                    .name(v.name())
                    .price(v.price())
                    .stockQuantity(v.stockQuantity())
                    .build();
            item.addVariant(variant);
        });

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item getItemById(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    @Transactional
    public void deleteItem(UUID id) {
        Item item = getItemById(id);
        itemRepository.delete(item);
    }

    @Transactional
    public void deductStock(List<StockDeductionRequest> deductions) {
        for (StockDeductionRequest deduction : deductions) {
            ItemVariant variant = itemVariantRepository.findBySku(deduction.sku())
                    .orElseThrow(() -> new ResourceNotFoundException("SKU not found: " + deduction.sku()));

            if (variant.getStockQuantity() < deduction.quantity()) {
                throw new InsufficientStockException("Insufficient stock for SKU: " + deduction.sku() +
                        ". Available: " + variant.getStockQuantity() + ", Requested: " + deduction.quantity());
            }

            variant.setStockQuantity(variant.getStockQuantity() - deduction.quantity());
            itemVariantRepository.save(variant);
        }
    }

    @Transactional
    public Item updateItem(UUID id, ItemRequest request) {
        Item existingItem = getItemById(id);

        existingItem.setName(request.name());
        existingItem.setDescription(request.description());

        if (request.variants() != null) {
            Set<String> requestedSkus = request.variants().stream()
                    .map(v -> v.sku())
                    .collect(Collectors.toSet());

            existingItem.getVariants().removeIf(v -> !requestedSkus.contains(v.getSku()));

            request.variants().forEach(vReq -> {
                existingItem.getVariants().stream()
                        .filter(v -> v.getSku().equals(vReq.sku()))
                        .findFirst()
                        .ifPresentOrElse(
                                existingVariant -> {
                                    existingVariant.setName(vReq.name());
                                    existingVariant.setPrice(vReq.price());
                                    existingVariant.setStockQuantity(vReq.stockQuantity());
                                },
                                () -> {
                                    ItemVariant newVariant = ItemVariant.builder()
                                            .sku(vReq.sku())
                                            .name(vReq.name())
                                            .price(vReq.price())
                                            .stockQuantity(vReq.stockQuantity())
                                            .item(existingItem)
                                            .build();
                                    existingItem.getVariants().add(newVariant);
                                }
                        );
            });
        } else {
            existingItem.getVariants().clear();
        }

        return itemRepository.save(existingItem);
    }
}