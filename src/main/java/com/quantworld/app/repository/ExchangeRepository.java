package com.quantworld.app.repository;

import com.quantworld.app.domain.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ExchangeRepository extends JpaRepository<Exchange, String> {

  String NAME = "exchangeRepository";

  Exchange findByExchange(String exchange);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("delete from Exchange " +
      "where accessKey=:accessKey " +
      "and secretKey=:secretKey " +
      "and exchange=:exchange " +
      "and type=:type ")
  void deleteExchangeByAccessKeyAndSecretKeyAndExchangeAndType(@Param("accessKey") String accessKey,
                                                               @Param("secretKey") String secretKey,
                                                               @Param("exchange") String exchange,
                                                               @Param("type") String type);
}
