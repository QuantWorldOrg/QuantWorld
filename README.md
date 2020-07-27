# 宽界(QuantWorld)[![LICENSE](https://img.shields.io/badge/License-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![Badge](https://img.shields.io/badge/Link-996.icu-red.svg)](https://996.icu/#/zh_CN)

[![使用IntelliJ IDEA开发维护](https://img.shields.io/badge/IntelliJ%20IDEA-提供支持-blue.svg)](https://www.jetbrains.com/?from=WxJava-weixin-java-tools)
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

# 环境部署
1. 开发环境部署
// TODO
2. 生产环境部署
// TODO
# 系统展示

- 主页
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-61.png)
- 交易所展示
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-59.png)
- 策略库
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-62.png)
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-63.png)
- 个人资产
![](https://github.com/SouthernYard/SouthernYard.github.io/blob/master/images/pasted-64.png)
