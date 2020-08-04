package com.quantworld.app.repository;

import com.quantworld.app.domain.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, String> {

  String NAME = "exchangeRepository";

  Exchange findByExchange(String exchange);
}
