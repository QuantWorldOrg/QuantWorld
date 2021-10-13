# Deprecated
1. 该项目存在架构缺陷和性能缺陷，已不再维护，有兴趣的朋友可以作为Demo项目玩玩。
2. 对于缺陷的我已在博文中有解释 [Xronos: 架构设计-1](https://southernyard.github.io/2020/09/08/Xronos%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1-1/)
3. 重构项目`Xronos`没有开源计划。

# 宽界(QuantWorld)[![LICENSE](https://img.shields.io/badge/License-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![Badge](https://img.shields.io/badge/Link-996.icu-red.svg)](https://996.icu/#/zh_CN)

[![使用IntelliJ IDEA开发维护](https://img.shields.io/badge/IntelliJ%20IDEA-提供支持-blue.svg)](https://www.jetbrains.com)
[![License](https://img.shields.io/badge/License-Apache--2.0-green)](https://opensource.org/licenses/Apache-2.0)

宽界系统（以下简称宽界）是一个基于Java的开源量化交易系统，系统的完成情况可以参考组件部分。宽界主要由Admin, Broker, CEP, OMS, Application和BackTestEngine等6个部分组成。目前只完成了系统的大部分基础框架，一些细节功能还在完善中。
在量化交易领域，看到了很多基于Python的系统，却很少有基于Java实现的系统，所以这是我设计宽界的初衷。
宽界目前只完成了数字货币的火币API的接入，后续会接入更多交易所的API，在框架的设计之初，股票期货等也是可以接入的，这也在未来的计划之中，非常欢迎有志的朋友一起合作，贡献代码。

# 宽界提供什么？

- 注册、登录、个人账户。
- 数字货币/期货/股票交易API的高可扩展。
- 策略自定义开发。
- 交易状态邮件提醒功能。
- 账户盈亏可视化展示。
- 多交易所接入。
- 多策略执行，多标的监控。
- 策略交易日志展示。
- 多交易所，多账户资产显示


# 宽界架构

![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-58.png)
技术栈：Vue/Bootstrap/jQuery/Thymeleaf/Spring Data Jpa/Spring Boot Mail/WebJars/Mysql/MongoDB/Tomcat/WebSocket

# 组件:

## Admin
- [X] AdminEngine

## Broker:
- [X] Gateway  
- [X] API
- [X] Router

## CEP
- [X] EventDispatcher
- [ ] SignalCalculator
- [ ] DataEngine
- [X] DataBase

## OMS
- [X] OMSEngine
- [X] RiskManagement
- [X] PositionManagement
- [X] EventProcessor

## Application
- [X] Monitor
- [X] Commander
- [ ] Demo Strategy

## BackTestEngine
- [ ] BackTestEngine
- [ ] Matcher
- [ ] Analyzer

# 策略开发
> [策略开发](https://github.com/QuantWorldOrg/QuantWorld/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E7%AD%96%E7%95%A5%E5%BC%80%E5%8F%91)

# 环境部署
> [开发环境部署](https://github.com/QuantWorldOrg/QuantWorld/wiki/%E5%BC%80%E5%8F%91%E7%8E%AF%E5%A2%83%E9%83%A8%E7%BD%B2)

> [生产环境部署](https://github.com/QuantWorldOrg/QuantWorld/wiki/%E7%94%9F%E4%BA%A7%E7%8E%AF%E5%A2%83%E9%83%A8%E7%BD%B2)

# 系统展示

- 主页
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-61.png)
- 交易- 交易所展示: 可以展示用户当前添加的所有交易所信息所展示
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-59.png)
- 策略库: 展示所有添加的交易策略, 并且所有添加的策略都是可以针对多个标的
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-62.png)
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-63.png)
- 代理设置: 为系统添加代理服务器信息
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-65.png)
- 个人资产: 个人资产展示
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-64.png)
