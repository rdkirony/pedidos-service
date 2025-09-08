-- =====================================================================
-- Tabela de usuários - USER_INFO
-- =====================================================================
CREATE TABLE IF NOT EXISTS `USER_INFO` (
    `ID`              CHAR(36)       NOT NULL,
    `FIRST_NAME`      VARCHAR(100)   NULL,
    `LAST_NAME`       VARCHAR(100)   NULL,
    `USERNAME`        VARCHAR(150)   NOT NULL,
    `PASSWORD`        VARCHAR(255)   NOT NULL,
    `ROLE`            INT            NOT NULL,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `UK_USER_INFO_USERNAME` (`USERNAME`)
    ) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- =====================================================================
-- Tabela de produtos - PRODUCT
-- =====================================================================
CREATE TABLE IF NOT EXISTS `PRODUCT` (
    `ID`              CHAR(36)       NOT NULL,
    `NAME`            VARCHAR(255)   NOT NULL,
    `DESCRIPTION`     TEXT           NULL,
    `PRICE`           DECIMAL(19,2)  NOT NULL,
    `CATEGORY`        VARCHAR(100)   NULL,
    `QUANTITY`        INT            NOT NULL DEFAULT 0,
    `CREATION_DATE`   DATETIME(6)    NOT NULL,
    `UPDATE_DATE`     DATETIME(6)    NULL,
    `VERSION`         BIGINT         NULL,
    PRIMARY KEY (`ID`),
    KEY `IDX_PRODUCT_CATEGORY` (`CATEGORY`)
    ) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- =====================================================================
-- Tabela de pedidos - ORDERS
-- =====================================================================
CREATE TABLE IF NOT EXISTS `ORDERS` (
    `ID`                 CHAR(36)       NOT NULL,
    `STATUS`             VARCHAR(50)    NOT NULL,
    `CREATION_DATE`      DATETIME(6)    NOT NULL,
    `COMPLETED_DATE`     DATETIME(6)    NULL,
    `VERSION`            BIGINT         NULL,
    `PAYMENT_IDEM_KEY`   VARCHAR(100)   NULL,
    `DETAILS`            TEXT           NULL,
    `USER_ID`            CHAR(36)       NOT NULL,
    PRIMARY KEY (`ID`),
    KEY `IDX_ORDERS_USER` (`USER_ID`),
    KEY `IDX_ORDERS_STATUS_COMPLETED` (`STATUS`, `COMPLETED_DATE`),
    CONSTRAINT `FK_ORDERS_USER`
    FOREIGN KEY (`USER_ID`) REFERENCES `USER_INFO`(`ID`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT
    ) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- =====================================================================
-- Itens do pedido - ORDEM_ITEM
-- =====================================================================
CREATE TABLE IF NOT EXISTS `ORDEM_ITEM` (
    `ID`          BIGINT         NOT NULL AUTO_INCREMENT,
    `ORDER_ID`    CHAR(36)       NOT NULL,
    `PRODUCT_ID`  CHAR(36)       NOT NULL,
    `QUANTITY`    INT            NOT NULL,
    `PRICE`       DECIMAL(19,2)  NOT NULL,
    PRIMARY KEY (`ID`),
    KEY `IDX_ORDEM_ITEM_ORDER` (`ORDER_ID`),
    KEY `IDX_ORDEM_ITEM_PRODUCT` (`PRODUCT_ID`),
    CONSTRAINT `FK_ORDEM_ITEM_ORDER`
    FOREIGN KEY (`ORDER_ID`) REFERENCES `ORDERS`(`ID`)
    ON UPDATE RESTRICT
    ON DELETE CASCADE,
    CONSTRAINT `FK_ORDEM_ITEM_PRODUCT`
    FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT`(`ID`)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT
    ) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;

-- =====================================================================
-- Idempotência de pagamento - PAYMENT_IDEM
-- =====================================================================
CREATE TABLE IF NOT EXISTS `PAYMENT_IDEM` (
    `ID`            BIGINT         NOT NULL AUTO_INCREMENT,
    `ORDER_ID`      CHAR(36)       NOT NULL,
    `IDEM_KEY`      VARCHAR(100)   NOT NULL,
    `CREATION_DATE` DATETIME(6)    NOT NULL,
    PRIMARY KEY (`ID`),
    UNIQUE KEY `UK_PAYMENT_IDEM_ORDER_KEY` (`ORDER_ID`, `IDEM_KEY`),
    KEY `IDX_PAYMENT_IDEM_ORDER` (`ORDER_ID`),
    CONSTRAINT `FK_PAYMENT_IDEM_ORDER`
    FOREIGN KEY (`ORDER_ID`) REFERENCES `ORDERS`(`ID`)
    ON UPDATE RESTRICT
    ON DELETE CASCADE
    ) ENGINE=InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;
