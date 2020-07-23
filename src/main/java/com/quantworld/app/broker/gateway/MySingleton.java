package com.quantworld.app.broker.gateway;

public class MySingleton {

  public enum MyEnumSingle{
    INSTANCE;
    private MySingleton singleOne;

    private MyEnumSingle(){
      System.out.println("初始化单例");
      singleOne = new MySingleton();
    }

    public MySingleton getInstance(){
      return singleOne;
    }
  }

  private MySingleton(){}

  public static MySingleton getInstance(){
    return MyEnumSingle.INSTANCE.getInstance();
  }

}