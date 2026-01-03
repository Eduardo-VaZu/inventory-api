package com.warehouse.inventory_api.mapper;

import org.mapstruct.*;

import com.warehouse.inventory_api.domain.Product;
import com.warehouse.inventory_api.dto.ProductRequestDTO;
import com.warehouse.inventory_api.dto.ProductResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductResponseDTO toResponse(Product product);

    @Mapping(target = "id", ignore = true) // El ID se genera automáticamente
    Product toEntity(ProductRequestDTO request);
}
