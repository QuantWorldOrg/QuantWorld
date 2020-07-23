package com.quantworld.app.trader.repository;

import com.quantworld.app.trader.domain.StrategyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Shawn
 * @Date: 12/20/2019
 * @Description:
 */
@Component
public interface StrategyEntityDAO {

  void save(StrategyEntity strategyEntity);

  boolean deleteByStrategyName(String strategyName, String userId);

  StrategyEntity getLastModifiedRecord(String userId);

  Long getMaxCountByStrategyTemplate(String strategyTemplateName, String userId);

  void update(StrategyEntity strategyEntity);

  List<StrategyEntity> findByStrategyName(String strategyName, String userId);

  List<StrategyEntity> findAll();

  StrategyEntity findByUUID(String id);

  Long getAmountByStrategyTemplate(String strategyTemplate);

  List<StrategyEntity> findAllStrategiesOrderByLastModifiedDateDesc(String userId);

}
