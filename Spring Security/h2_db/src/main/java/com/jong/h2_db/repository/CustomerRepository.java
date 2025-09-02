package com.jong.h2_db.repository;

import com.jong.h2_db.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByIsActiveTrue();

    List<Customer> findByIsActiveFalse();

    List<Customer> findByNameContainingIgnoreCase(String name);

    Optional<Customer> findByPhone(String phone);

    List<Customer> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT c FROM Customer c WHERE YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) >= :age")
    List<Customer> findCustomersOlderThan(@Param("age") int age);

    @Query("SELECT c FROM Customer c WHERE c.address IS NOT NULL AND c.address != ''")
    List<Customer> findCustomersWithAddress();

    @Query("SELECT SUBSTRING(c.email, LOCATE('@', c.email) + 1) as domain, COUNT(c) " +
            "FROM Customer c GROUP BY SUBSTRING(c.email, LOCATE('@', c.email) + 1)")
    List<Object[]> countCustomersByEmailDomain();

    @Query(value = "SELECT MONTH(created_at) as month, COUNT(*) as count " +
            "FROM customers GROUP BY MONTH(created_at) ORDER BY month",
            nativeQuery = true)
    List<Object[]> getMonthlyRegistrationStats();
}