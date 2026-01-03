package com.warehouse.inventory_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.inventory_api.domain.Category;
import com.warehouse.inventory_api.domain.Product;
import com.warehouse.inventory_api.dto.ProductRequestDTO;
import com.warehouse.inventory_api.dto.ProductResponseDTO;
import com.warehouse.inventory_api.mapper.ProductMapper;
import com.warehouse.inventory_api.repository.ProductRepository;
import com.warehouse.inventory_api.repository.spec.ProductSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

        private final ProductRepository productRepository;
        private final ProductMapper productMapper;

        // Carga Masiva
        @Transactional
        public List<ProductResponseDTO> createProducts(List<ProductRequestDTO> requests) {
                // Convertimos lista de DTOs a lista de Entidades
                List<Product> products = requests.stream()
                                .map(productMapper::toEntity)
                                .toList();

                // Guardamos todo de una vez (batch insert implícito de JPA)
                List<Product> savedProducts = productRepository.saveAll(products);

                return savedProducts.stream()
                                .map(productMapper::toResponse)
                                .toList();
        }

        // Consulta con Filtros Dinámicos + Paginación
        @Transactional(readOnly = true)
        public Page<ProductResponseDTO> getProducts(
                        Pageable pageable,
                        Category category,
                        BigDecimal minPrice,
                        String name) {
                // 1. Construir la Specification combinando condiciones (AND)
                Specification<Product> spec = Specification.where(ProductSpecifications.hasCategory(category))
                                .and(ProductSpecifications.priceGreaterThan(minPrice))
                                .and(ProductSpecifications.nameContains(name));

                // 2. Ejecutar consulta paginada
                Page<Product> productPage = productRepository.findAll(spec, pageable);

                // 3. Convertir la página de entidades a página de DTOs
                return productPage.map(productMapper::toResponse);
        }
}
