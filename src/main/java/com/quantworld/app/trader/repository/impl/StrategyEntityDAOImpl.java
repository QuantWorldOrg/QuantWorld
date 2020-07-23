package com.quantworld.app.trader.repository.impl;

import com.quantworld.app.data.constants.CollectionName;
import com.quantworld.app.trader.domain.StrategyEntity;
import com.quantworld.app.trader.repository.StrategyEntityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: Shawn
 * @Date: 12/21/2019
 * @Description:
 */
@Repository
public class StrategyEntityDAOImpl implements StrategyEntityDAO {

  MongoTemplate mongoTemplate;

  @Autowired
  public StrategyEntityDAOImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void save(StrategyEntity strategyEntity) {
    mongoTemplate.save(strategyEntity, CollectionName.STRATEGYENTITY);
  }

  @Override
  public boolean deleteByStrategyName(String strategyName, String userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.STRATEGY_NAME).is(strategyName));
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(userId));
    long deletedNumber = mongoTemplate.remove(query, StrategyEntity.class, CollectionName.STRATEGYENTITY).getDeletedCount();
    return deletedNumber >= 0;
  }

  @Override
  public StrategyEntity getLastModifiedRecord(String userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(userId));
    List<StrategyEntity> strategyEntityList = mongoTemplate.find(query, StrategyEntity.class,
        CollectionName.STRATEGYENTITY);
    Collections.sort(strategyEntityList);
    if (strategyEntityList.size() > 0) {
      return strategyEntityList.get(strategyEntityList.size() - 1);
    } else {
      return null;
    }
  }

  @Override
  public Long getMaxCountByStrategyTemplate(String strategyTemplateName, String userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.STRATEGY_TEMPLATE).is(strategyTemplateName));
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(userId));
    query.with(Sort.by(Sort.Direction.DESC, StrategyEntity._COUNT));
    query.limit(1);
    StrategyEntity strategyEntity = mongoTemplate.findOne(query, StrategyEntity.class,
        CollectionName.STRATEGYENTITY);
    if (strategyEntity != null) {
      return strategyEntity.getCount();
    } else {
      return 0L;
    }
  }

  @Override
  public void update(StrategyEntity strategyEntity) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.STRATEGY_NAME).is(strategyEntity.getStrategyName()));
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(strategyEntity.getUserId()));
    query.addCriteria(Criteria.where(StrategyEntity.ID).is(strategyEntity.getUuid()));
    Update update = new Update();
    update.set(StrategyEntity.STRATEGY_NAME, strategyEntity.getStrategyName());
    update.set(StrategyEntity.STRATEGY_PARAM, strategyEntity.getStrategyParam());
    update.set(StrategyEntity.LAST_MODIFIED_DATE, new Date());
    mongoTemplate.findAndModify(query, update, StrategyEntity.class, CollectionName.STRATEGYENTITY);
  }

  @Override
  public List<StrategyEntity> findByStrategyName(String strategyName, String userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.STRATEGY_NAME).is(strategyName));
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(userId));
    return mongoTemplate.find(query, StrategyEntity.class, CollectionName.STRATEGYENTITY);
  }

  @Override
  public List<StrategyEntity> findAll() {
    return mongoTemplate.findAll(StrategyEntity.class);
  }

  @Override
  public StrategyEntity findByUUID(String id) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.ID).is(id));
    List<StrategyEntity> strategyEntityList = mongoTemplate.find(query, StrategyEntity.class,
        CollectionName.STRATEGYENTITY);
    if (!strategyEntityList.isEmpty()) {
      return strategyEntityList.get(0);
    } else {
      return null;
    }
  }

  @Override
  public Long getAmountByStrategyTemplate(String strategyTemplateName) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.STRATEGY_TEMPLATE).is(strategyTemplateName));
    return mongoTemplate.count(query, StrategyEntity.class);
  }

  @Override
  public List<StrategyEntity> findAllStrategiesOrderByLastModifiedDateDesc(String userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(StrategyEntity.USER_ID).is(userId));
    List<StrategyEntity> strategyEntityList = mongoTemplate.find(query, StrategyEntity.class,
        CollectionName.STRATEGYENTITY);
    //Sort by lastmodifiedDate
    Collections.sort(strategyEntityList);
    return strategyEntityList;
  }
}
