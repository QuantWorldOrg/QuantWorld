package com.quantworld.app.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface StrategyBasicParamGenerator {
  JSONObject getBasicStrategyParam(String strategyTemplateName);
}
