package com.jong.h2_db.service;

import com.jong.h2_db.model.Customer;
import com.jong.h2_db.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(String name, String email, String phone, LocalDate dateOfBirth, String address) {
        log.debug("Creating new customer: {}", email);
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다: " + email);
        }

        Customer customer = new Customer(name, email, phone, dateOfBirth, address);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        log.debug("Finding customer by id: {}", id);
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByEmail(String email) {
        log.debug("Finding customer by email: {}", email);
        return customerRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllActiveCustomers() {
        log.debug("Finding all active customers");
        return customerRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Customer> searchCustomersByName(String name) {
        log.debug("Searching customers by name: {}", name);
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomersOlderThan(int age) {
        log.debug("Finding customers older than: {}", age);
        return customerRepository.findCustomersOlderThan(age);
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomersWithAddress() {
        log.debug("Finding customers with address");
        return customerRepository.findCustomersWithAddress();
    }

    public Customer updateCustomer(Long id, String name, String phone, String address) {
        log.debug("Updating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("고객을 찾을 수 없습니다: " + id));

        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);

        return customerRepository.save(customer);
    }

    public Customer deactivateCustomer(Long id) {
        log.debug("Deactivating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("고객을 찾을 수 없습니다: " + id));

        customer.deactivate();
        return customerRepository.save(customer);
    }

    public Customer activateCustomer(Long id) {
        log.debug("Activating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("고객을 찾을 수 없습니다: " + id));

        customer.activate();
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public long getTotalCustomerCount() {
        log.debug("Counting total customers");
        return customerRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveCustomerCount() {
        log.debug("Counting active customers");
        return customerRepository.findByIsActiveTrue().size();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyRegistrationStats() {
        log.debug("Getting monthly registration statistics");
        return customerRepository.getMonthlyRegistrationStats();
    }
}