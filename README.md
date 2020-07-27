# 宽界(QuantWorld)
![license](https://img.shields.io/badge/License-Apache--2.0-green)

宽界系统（以下简称宽界）是一个基于Java的开源量化交易系统，系统的完成情况可以参考组件部分。宽界主要由Admin, Broker, CEP, OMS, Application和BackTestEngine等6个部分组成。目前只完成了系统的大部分基础框架，一些细节功能还在完善中。
在量化交易领域，看到了很多基于Python的系统，却很少有基于Java实现的系统，所以这是我设计宽界的初衷。
宽界目前只完成了数字货币的火币API的接入，后续会接入更多交易所的API，在框架的设计之初，股票期货等也是可以接入的，这也在未来的计划之中，非常欢迎有志的朋友一起合作，贡献代码。

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
