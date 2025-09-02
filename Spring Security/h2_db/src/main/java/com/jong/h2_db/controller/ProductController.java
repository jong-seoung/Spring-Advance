package com.jong.h2_db.controller;

import com.jong.h2_db.model.Product;
import com.jong.h2_db.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllActiveProducts() {
        log.info("GET /api/products - Getting all active products");
        return productService.getAllActiveProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Getting product by id", id);
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        log.info("GET /api/products/search - Searching products with name: {}", name);
        return productService.searchProductsByName(name);
    }

    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(@RequestParam BigDecimal minPrice,
                                                 @RequestParam BigDecimal maxPrice) {
        log.info("GET /api/products/price-range - Getting products in price range: {} - {}", minPrice, maxPrice);
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts() {
        log.info("GET /api/products/low-stock - Getting low stock products");
        return productService.getLowStockProducts();
    }

    @GetMapping("/out-of-stock")
    public List<Product> getOutOfStockProducts() {
        log.info("GET /api/products/out-of-stock - Getting out of stock products");
        return productService.getOutOfStockProducts();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam String name,
                                                 @RequestParam String description,
                                                 @RequestParam BigDecimal price,
                                                 @RequestParam Integer stockQuantity) {
        log.info("POST /api/products - Creating new product: {}", name);
        try {
            Product product = productService.createProduct(name, description, price, stockQuantity);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id,
                                               @RequestParam Integer newStock) {
        log.info("PUT /api/products/{}/stock - Updating stock to: {}", id, newStock);
        try {
            Product product = productService.updateStock(id, newStock);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Error updating stock for product {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Product> deactivateProduct(@PathVariable Long id) {
        log.info("PUT /api/products/{}/deactivate - Deactivating product", id);
        try {
            Product product = productService.deactivateProduct(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Error deactivating product {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getActiveProductCount() {
        log.info("GET /api/products/stats/count - Getting active product count");
        long count = productService.getActiveProductCount();
        return ResponseEntity.ok(count);
    }
}