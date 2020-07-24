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
package com.quantworld.app.repository;

import com.quantworld.app.domain.HistoricalTradeRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface HistoricalTradeRecordsRepository extends JpaRepository<HistoricalTradeRecords, String> {
  @Override
  List<HistoricalTradeRecords> findAll();

  @Override
  <S extends HistoricalTradeRecords> S save(S s);

  List<HistoricalTradeRecords> findByStrategyName(String strategyName);

  List<HistoricalTradeRecords> findByStrategyNameAndCreatedTimeAfterAndCreatedTimeBeforeOrderByCreatedTimeDesc(String strategyName, Timestamp startTimeStamp, Timestamp endTimeStamp);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("SELECT SUM(price * volume) AS historicalTradingVolume, SUM(profit) AS historicalTradingProfit, DATE(createdTime) AS createdDate " +
      "FROM HistoricalTradeRecords " +
      "WHERE strategyName=:strategyName " +
      "GROUP BY DATE(createdTime) " +
      "ORDER BY DATE(createdTime)")
  List getAllTradingAndProfitRecordGroupByCreateDate(@Param("strategyName") String strategyName);


  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("SELECT SUM(price * volume) AS historicalTradingVolume, SUM(profit) AS historicalTradingProfit, DATE(createdTime) " +
      "AS createdDate " +
      "FROM HistoricalTradeRecords " +
      "WHERE strategyName=:strategyName " +
      "AND DATE(createdTime)>=:startDate " +
      "AND DATE(createdTime)<=:endDate " +
      "GROUP BY DATE(createdTime)  " +
      "ORDER BY DATE(createdTime)")
  List getAllTradingAndProfitDataBetweenStartAndEndDateGroupByCreatedDate(@Param("strategyName") String strategyName,
                                                                          @Param("startDate") Date startDate,
                                                                          @Param("endDate") Date endDate);

}
