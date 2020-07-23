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
