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
