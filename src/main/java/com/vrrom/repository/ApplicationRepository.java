package com.vrrom.repository;

import com.vrrom.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Application findById(long id);

}
