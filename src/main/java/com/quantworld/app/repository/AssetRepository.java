package com.quantworld.app.repository;

import com.quantworld.app.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface AssetRepository extends JpaRepository<Asset, String> {

  Asset findByUserNameAndExChangeName(String useName, String exChangeName);
}
