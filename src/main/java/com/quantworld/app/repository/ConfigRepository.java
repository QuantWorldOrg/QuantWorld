package com.quantworld.app.repository;

import com.quantworld.app.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ConfigRepository extends JpaRepository<Config, String> {

  Config findByUserId(String userId);
}
