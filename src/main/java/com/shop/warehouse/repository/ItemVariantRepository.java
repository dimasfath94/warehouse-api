package com.shop.warehouse.repository;

import com.shop.warehouse.entity.ItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemVariantRepository extends JpaRepository<ItemVariant, UUID> {
    Optional<ItemVariant> findBySku(String sku);
}