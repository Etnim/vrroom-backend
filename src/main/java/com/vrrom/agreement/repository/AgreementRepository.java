package com.vrrom.agreement.repository;

import com.vrrom.agreement.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
