package com.jong.h2_db.config;

import com.jong.h2_db.model.Customer;
import com.jong.h2_db.model.Product;
import com.jong.h2_db.model.ProductStatus;
import com.jong.h2_db.repository.CustomerRepository;
import com.jong.h2_db.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeProducts();
        initializeCustomers();

        log.info("=".repeat(60));
        log.info("✅ 테스트 데이터 초기화 완료!");
        log.info("📊 상품 데이터: {}개", productRepository.count());
        log.info("👥 고객 데이터: {}개", customerRepository.count());
        log.info("🌐 H2 Console: http://localhost:8080/h2-console");
        log.info("🔐 JDBC URL: jdbc:h2:mem:testdb");
        log.info("👤 Username: sa");
        log.info("🔑 Password: h2demo");
        log.info("=".repeat(60));
    }

    private void initializeProducts() {
        if (productRepository.count() > 0) {
            log.debug("Products already initialized, skipping...");
            return;
        }

        log.debug("Initializing product data...");

        productRepository.save(new Product("iPhone 15 Pro", "Apple의 최신 플래그십 스마트폰",
                new BigDecimal("1200000"), 25));
        productRepository.save(new Product("Galaxy S24 Ultra", "삼성의 프리미엄 스마트폰",
                new BigDecimal("1300000"), 18));
        productRepository.save(new Product("MacBook Air M3", "애플 MacBook Air 최신 모델",
                new BigDecimal("1500000"), 12));

        productRepository.save(new Product("나이키 에어맥스", "편안한 러닝화",
                new BigDecimal("150000"), 50));
        productRepository.save(new Product("아디다스 후드티", "캐주얼 후드 스웨트셔츠",
                new BigDecimal("89000"), 30));

        productRepository.save(new Product("스프링 부트 완전정복", "Spring Boot 개발 가이드북",
                new BigDecimal("35000"), 100));
        productRepository.save(new Product("자바 프로그래밍", "초보자를 위한 Java 입문서",
                new BigDecimal("28000"), 75));

        Product outOfStock = new Product("인기 상품 (품절)", "매우 인기있는 상품",
                new BigDecimal("99000"), 0);
        productRepository.save(outOfStock);

        Product inactiveProduct = new Product("단종 상품", "더이상 판매하지 않는 상품",
                new BigDecimal("50000"), 5);
        inactiveProduct.setStatus(ProductStatus.INACTIVE);
        productRepository.save(inactiveProduct);

        log.debug("Product data initialization completed");
    }

    private void initializeCustomers() {
        if (customerRepository.count() > 0) {
            log.debug("Customers already initialized, skipping...");
            return;
        }

        log.debug("Initializing customer data...");

        customerRepository.save(new Customer("김철수", "kim.cheolsu@example.com",
                "010-1234-5678", LocalDate.of(1990, 3, 15), "서울시 강남구"));
        customerRepository.save(new Customer("이영희", "lee.younghee@example.com",
                "010-2345-6789", LocalDate.of(1985, 7, 22), "부산시 해운대구"));
        customerRepository.save(new Customer("박민수", "park.minsu@example.com",
                "010-3456-7890", LocalDate.of(1992, 11, 8), "대구시 중구"));
        customerRepository.save(new Customer("정소영", "jung.soyoung@gmail.com",
                "010-4567-8901", LocalDate.of(1988, 5, 30), "인천시 연수구"));
        customerRepository.save(new Customer("최대현", "choi.daehyun@naver.com",
                "010-5678-9012", LocalDate.of(1995, 1, 12), null));

        Customer inactiveCustomer = new Customer("홍길동", "hong.gildong@example.com",
                "010-6789-0123", LocalDate.of(1980, 9, 5), "광주시 서구");
        inactiveCustomer.deactivate();
        customerRepository.save(inactiveCustomer);

        log.debug("Customer data initialization completed");
    }
}