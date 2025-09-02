package com.jong.h2_db.repository;

import com.jong.h2_db.model.Product;
import com.jong.h2_db.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByStockQuantityLessThan(Integer quantity);

    Optional<Product> findByNameAndStatus(String name, ProductStatus status);

    @Query("SELECT p FROM Product p WHERE p.price < :maxPrice AND p.status = 'ACTIVE'")
    List<Product> findActiveProductsUnderPrice(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = :status")
    long countByStatus(@Param("status") ProductStatus status);

    @Query(value = "SELECT * FROM products WHERE stock_quantity = 0", nativeQuery = true)
    List<Product> findOutOfStockProducts();

    @Query("SELECT p FROM Product p WHERE p.stockQuantity < 10 AND p.status = 'ACTIVE'")
    List<Product> findLowStockProducts();
}