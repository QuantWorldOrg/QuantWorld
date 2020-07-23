package com.quantworld.app.repository;

import com.quantworld.app.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, String> {
  @Override
  List<Contract> findAll();

  @Override
  <S extends Contract> S save(S s);
}
