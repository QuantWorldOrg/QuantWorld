/*
 * Copyright 2019-2020 Shawn Peng
 * Email: shawnpeng@yeah.net
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
