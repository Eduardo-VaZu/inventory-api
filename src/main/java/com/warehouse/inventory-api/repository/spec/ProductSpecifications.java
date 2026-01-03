package com.warehouse.inventory_api.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.warehouse.inventory_api.domain.Category;
import com.warehouse.inventory_api.domain.Product;

public class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> hasCategory(Category category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            // SQL equivalente: WHERE category = 'ELECTRONICS'
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Product> priceGreaterThan(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return null;
            }
            // SQL equivalente: WHERE price >= 100.00
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    // Un extra útil: Búsqueda por nombre (contiene texto, ignorando
    // mayúsculas/minúsculas)
    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }
            // SQL equivalente: WHERE LOWER(name) LIKE '%iphone%'
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

}
