package com.vrrom.customer.repository;

import com.vrrom.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
