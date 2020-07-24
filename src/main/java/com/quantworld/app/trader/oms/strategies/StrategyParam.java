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
package com.quantworld.app.trader.oms.strategies;

import com.quantworld.app.QWContext;
import com.quantworld.app.data.ContractData;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.StrategyTemplateEnum;
import com.quantworld.app.trader.oms.EventProcessor;

/**
 * The setting can be modified by user, this kind of param should be declared here.
 * For low coupling, high scaling, no hard-coded, evey strategy should extend this super class.
 * 
 * @author: Shawn
 * @Date: 12/9/2019
 * @Description:
 */
public abstract class StrategyParam {

  protected ExchangeEnum exchangeEnum;

  protected boolean strategyRunningFlag = false;

  public abstract StrategyTemplateEnum getStrategyTemplateName();

  public ExchangeEnum getExchangeEnum() {
    return exchangeEnum;
  }

  public void setExchangeEnum(ExchangeEnum exchangeEnum) {
    this.exchangeEnum = exchangeEnum;
  }

  public ContractData getContract(String qwSymbol) {
    EventProcessor eventProcessor = (EventProcessor) QWContext.getBean(EventProcessor.NAME);
    return eventProcessor.getContract(null, qwSymbol);
  }

  public boolean getStrategyRunningFlag() {
    return strategyRunningFlag;
  }

  public void setStrategyRunningFlag(boolean strategyRunningFlag) {
    this.strategyRunningFlag = strategyRunningFlag;
  }
}
