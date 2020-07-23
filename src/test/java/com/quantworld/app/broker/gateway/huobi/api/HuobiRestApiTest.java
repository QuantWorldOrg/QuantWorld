package com.quantworld.app.broker.gateway.huobi.api;


import com.quantworld.app.broker.gateway.LocalOrderManager;
import com.quantworld.app.broker.gateway.huobi.HuobiGateway;
import com.quantworld.app.data.CancelRequest;
import com.quantworld.app.data.OrderRequest;
import com.quantworld.app.data.constants.DirectionEnum;
import com.quantworld.app.data.constants.ExchangeEnum;
import com.quantworld.app.data.constants.OrderTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HuobiRestApiTest {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String accountId = "1319204";

  private HuobiRestApi huobiRestApi;

  private String symbol = "akrousdt";

  @Test
  public void testQueryAccount() {
    HuobiRestApi huobiRestApi = new HuobiRestApi(new HuobiGateway());
    huobiRestApi.queryAccount();
  }

  @Test
  public void testQueryContract() {
    HuobiRestApi huobiRestApi = new HuobiRestApi(new HuobiGateway());
    huobiRestApi.queryContract();
  }

  @Test
  public void testQueryOrders() {
    HuobiRestApi huobiRestApi = new HuobiRestApi(new HuobiGateway());
    huobiRestApi.queryOrder();
  }

  @Test
  public void testQueryAccountBalance() {
    HuobiRestApi huobiRestApi = new HuobiRestApi(new HuobiGateway());
    huobiRestApi.setAccountId(accountId);
    huobiRestApi.queryAccountBalance();
  }

  @Test
  public void testSendOrder() {
    sendOrder();
  }

  @Test
  public void testCancelOrder() throws InterruptedException {

    String qwOrderId = sendOrder();
    Thread.sleep(5000L);
    CancelRequest request = new CancelRequest(qwOrderId.replace("huobi.", ""), symbol, ExchangeEnum.HUOBI);
    huobiRestApi.cancelOrder(request);
  }

  private String sendOrder() {
    HuobiGateway huobiGateway = new HuobiGateway();
    huobiGateway.setOrderManager(new LocalOrderManager(huobiGateway));
    huobiRestApi = new HuobiRestApi(huobiGateway);

    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setDirection(DirectionEnum.LONG);
    orderRequest.setType(OrderTypeEnum.LIMIT);
    orderRequest.setVolume(5000f);
    orderRequest.setExchange(ExchangeEnum.HUOBI);
    orderRequest.setSymbol(symbol);
    orderRequest.setPrice(0.001f);
    huobiRestApi.setAccountId(accountId);

    return huobiRestApi.sendOrder(orderRequest);
  }

  @Test
  public void testGetOpenOrders() {
    HuobiGateway huobiGateway = new HuobiGateway();
    huobiGateway.setOrderManager(new LocalOrderManager(huobiGateway));
    huobiRestApi = new HuobiRestApi(huobiGateway);

    logger.info(huobiRestApi.getOpenOrders());
  }


  @Test
  public void testGetContracts() {
    HuobiGateway huobiGateway = new HuobiGateway();
//    huobiGateway.setOrderManager(new LocalOrderManager(huobiGateway));
    huobiRestApi = new HuobiRestApi(huobiGateway);
    huobiRestApi.queryContract();
  }
}