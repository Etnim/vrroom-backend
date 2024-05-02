package com.vrrom.agreement;

import com.vrrom.application.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
