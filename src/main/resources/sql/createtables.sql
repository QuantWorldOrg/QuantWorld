# Some SQL to create table for set up basic DB environment, and will add some foreign key or index later to enhance the
# DB table design

# asset, save the individual asset
CREATE TABLE `asset`
(
  `id`             varchar(50) NOT NULL,
  `userName`       varchar(50)  DEFAULT NULL,
  `exChangeName`   varchar(50)  DEFAULT NULL,
  `accessKey`      varchar(255) DEFAULT NULL,
  `secretKey`      varchar(255) DEFAULT NULL,
  `assetPassword`  varchar(255) DEFAULT NULL,
  `createTime`     bigint(20)   DEFAULT NULL,
  `lastModifyTime` bigint(20)   DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

# exchange table, save the exchangs information
CREATE TABLE `exchange`
(
  `id`                  varchar(50) NOT NULL,
  `userName`            varchar(255) DEFAULT NULL,
  `exchangeName`        varchar(50)  DEFAULT NULL,
  `digitalCurrencyName` varchar(50)  DEFAULT NULL,
  `buyOrSell`           tinyint(4)   DEFAULT NULL,
  `dealStatus`          varchar(5)   DEFAULT NULL,
  `strategyName`        varchar(50)  DEFAULT NULL,
  `currentBalance`      int(11)      DEFAULT NULL,
  `originalBalance`     int(11)      DEFAULT NULL,
  `createTime`          bigint(20)   DEFAULT NULL,
  `lastModifyTime`      bigint(20)   DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

# strategy, save the strategy information
CREATE TABLE `strategy`
(
  `id`             varchar(50) NOT NULL,
  `strategyName`   varchar(255) DEFAULT NULL,
  `score`          smallint(4)  DEFAULT NULL,
  `createTime`     bigint(20)  NOT NULL,
  `lastModifyTime` bigint(20)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

# user table, save the users information
CREATE TABLE `user`
(
  `id`                varchar(50) NOT NULL,
  `username`          varchar(50)  DEFAULT NULL,
  `password`          varchar(255) DEFAULT NULL,
  `email`             varchar(100) DEFAULT NULL,
  `createTime`        bigint(20)  NOT NULL,
  `lastModifyTime`    bigint(20)  NOT NULL,
  `profilePicture`    varchar(100) DEFAULT NULL,
  `introduction`      mediumtext,
  `validataCode`      varchar(100) DEFAULT NULL,
  `backgroundPicture` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

# config table for personal user.
CREATE TABLE `config`
(
  `id`             varchar(50) NOT NULL,
  `userId`         varchar(50)  DEFAULT NULL,
  `defaultModel`   varchar(255) DEFAULT NULL,
  `createTime`     bigint(20)  NOT NULL,
  `lastModifyTime` bigint(20)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;