package com.quantworld.app.broker.gateway.bigone;

import com.quantworld.app.broker.gateway.BaseGateway;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.HistoryRequest;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.SubscribeRequest;
import com.quantworld.app.data.constants.ExchangeEnum;

import java.util.List;
import java.util.Map;

public class BigOneGateway extends BaseGateway {

  private static final String GATEWAY_NAME = ExchangeEnum.BIGONE.getGatewayName();

  public BigOneGateway() {
    super(GATEWAY_NAME);
  }

  @Override
  public void connect(Map<String, String> setting) {

  }

  @Override
  public void subscribe(SubscribeRequest request) {

  }

  @Override
  public void unSubscribe(SubscribeRequest request) {

  }

  @Override
  public String sendOrder(OrderRequest request) {
    return null;
  }

  @Override
  public void cancelOrder(CancelRequest cancelRequest) {

  }

  @Override
  public void queryAccount() {

  }

  @Override
  public void queryPosition() {

  }

  @Override
  public void queryContract() {

  }

  @Override
  public List<String> queryHistory(HistoryRequest request) {
    return null;
  }

  @Override
  public void close() {

  }
}
