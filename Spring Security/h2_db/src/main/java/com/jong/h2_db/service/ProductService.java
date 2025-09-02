package com.jong.h2_db.service;

import com.jong.h2_db.model.Product;
import com.jong.h2_db.model.ProductStatus;
import com.jong.h2_db.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(String name, String description, BigDecimal price, Integer stockQuantity) {
        log.debug("Creating new product: {}", name);
        Product product = new Product(name, description, price, stockQuantity);
        product.setStatus(ProductStatus.ACTIVE);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        log.debug("Finding all active products");
        return productRepository.findByStatus(ProductStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String name) {
        log.debug("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Finding products in price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        log.debug("Finding low stock products");
        return productRepository.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> getOutOfStockProducts() {
        log.debug("Finding out of stock products");
        return productRepository.findOutOfStockProducts();
    }

    public Product updateProduct(Long id, String name, String description, BigDecimal price, Integer stockQuantity) {
        log.debug("Updating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);

        return productRepository.save(product);
    }

    public Product updateStock(Long id, Integer newStock) {
        log.debug("Updating stock for product {}: {}", id, newStock);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        product.setStockQuantity(newStock);
        return productRepository.save(product);
    }

    public Product deactivateProduct(Long id) {
        log.debug("Deactivating product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        product.setStatus(ProductStatus.INACTIVE);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.debug("Deleting product: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("상품을 찾을 수 없습니다: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getActiveProductCount() {
        log.debug("Counting active products");
        return productRepository.countByStatus(ProductStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Product> getActiveProductsUnderPrice(BigDecimal maxPrice) {
        log.debug("Finding active products under price: {}", maxPrice);
        return productRepository.findActiveProductsUnderPrice(maxPrice);
    }
}