package com.shop.warehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record VariantRequest(
        @NotBlank(message = "SKU is required")
        String sku,

        @NotBlank(message = "Variant name is required")
        String name,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be zero or positive")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock must be zero or positive")
        Integer stockQuantity
) {}