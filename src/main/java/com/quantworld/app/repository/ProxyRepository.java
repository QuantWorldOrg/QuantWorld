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

package com.quantworld.app.repository;

import com.quantworld.app.domain.ProxyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProxyRepository extends JpaRepository<ProxyConfiguration, String> {

  String NAME = "proxyRepository";

  @Override
  List<ProxyConfiguration> findAll();

  @Modifying(clearAutomatically=true)
  @Transactional
  @Query("update ProxyConfiguration set protocol=:protocol, server=:server, port=:port where id=:id")
  void updateProtocolAndServerAndPortById(@Param("protocol") String protocol, @Param("server") String server, @Param("port") String port, @Param("id") String id);

  @Override
  <S extends ProxyConfiguration> S save(S s);
}
