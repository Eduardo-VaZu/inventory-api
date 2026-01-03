package com.warehouse.inventory_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.inventory_api.domain.Category;
import com.warehouse.inventory_api.dto.ProductRequestDTO;
import com.warehouse.inventory_api.dto.ProductResponseDTO;
import com.warehouse.inventory_api.service.ProductService;
import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint de Carga Masiva (para tener datos de prueba rápido)
    // POST /products
    @PostMapping
    public ResponseEntity<List<ProductResponseDTO>> createBulk(@Valid @RequestBody List<ProductRequestDTO> requests) {
        return new ResponseEntity<>(productService.createProducts(requests), HttpStatus.CREATED);
    }

    // Endpoint Estrella: Paginación + Filtros + Ordenamiento
    // GET /products?page=0&size=10&sort=price,desc&category=ELECTRONICS&minPrice=50
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.getProducts(pageable, category, minPrice, name));
    }
}