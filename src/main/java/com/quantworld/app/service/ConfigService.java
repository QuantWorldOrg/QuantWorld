package com.quantworld.app.service;

import com.quantworld.app.domain.Config;

public interface ConfigService {
  Config saveConfig(String userId);
  void updateConfig();
}
