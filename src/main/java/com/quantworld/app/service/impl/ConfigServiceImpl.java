package com.quantworld.app.service.impl;

import com.quantworld.app.domain.Config;
import com.quantworld.app.service.ConfigService;
import com.quantworld.app.repository.ConfigRepository;
import com.quantworld.app.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

  @Autowired
  ConfigRepository configRepository;
  @Override
  public Config saveConfig(String userId) {
    Config config = new Config();
    config.setUserId(userId);
    config.setDefaultModel("simple");
    config.setCreateTime(DateUtils.getCurrentTime());
    config.setLastModifyTime(DateUtils.getCurrentTime());
    configRepository.save(config);
    return config;
  }

  @Override
  public void updateConfig() {

  }
}
