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
package com.quantworld.app.trader.domain;

import com.quantworld.app.data.constants.CollectionName;
import com.quantworld.app.trader.oms.strategies.StrategyParam;
import com.quantworld.app.utils.QuantStringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;


/**
 * @author: Shawn
 * @Date: 12/20/2019
 * @Description:
 */
@Document(collection = CollectionName.STRATEGYENTITY)
public class StrategyEntity implements Comparable<StrategyEntity> {

  // same field name with StrategyEntity
  public static final String STRATEGY_NAME = "strategyName";

  // same field name with StrategyEntity
  public static final String STRATEGY_PARAM = "strategyParam";

  public static final String CREATED_DATE = "createdDate";

  public static final String LAST_MODIFIED_DATE = "lastModifiedDate";

  public static final String USER_ID = "userId";

  public static final String _COUNT = "count";

  public static final String ID = "uuid";

  public static final String STRATEGY_TEMPLATE = "strategyTemplate";

  private String uuid = UUID.randomUUID().toString();

  private String userId;

  private Long count;

  private String strategyName;

  private StrategyParam strategyParam;

  private String strategyTemplate;

  private Date createdDate = new Date();

  private Date lastModifiedDate = new Date();

  public StrategyEntity(String strategyName, @NotNull StrategyParam strategyParam) {
    this.strategyName = strategyName;
    this.strategyParam = strategyParam;
    String rgex = "_(.*)";
    this.count = Long.parseLong(QuantStringUtil.getSubUtilSimple(strategyName, rgex, 1));
    this.strategyTemplate = strategyParam.getStrategyTemplateName().toString();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getStrategyName() {
    return strategyName;
  }

  public void setStrategyName(String strategyName) {
    this.strategyName = strategyName;
  }

  public StrategyParam getStrategyParam() {
    return strategyParam;
  }

  public void setStrategyParam(StrategyParam strategyParam) {
    this.strategyParam = strategyParam;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  public int compareTo(@NotNull StrategyEntity strategyEntity) {
    return this.lastModifiedDate.compareTo(strategyEntity.lastModifiedDate);
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }
}
