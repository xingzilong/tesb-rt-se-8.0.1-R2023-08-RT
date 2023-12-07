--
-- #%L
-- Service Activity Monitoring :: Server
-- %%
-- Copyright (c) 2006-2021 Talend Inc. - www.talend.com
-- %%
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- #L%
--

CREATE SEQUENCE EVENT_ID
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1 CACHE 20;

CREATE TABLE EVENTS
(
    ID                NUMBER(19) NOT NULL,
    EI_TIMESTAMP      TIMESTAMP,
    EI_EVENT_TYPE     NVARCHAR2(255),
    ORIG_CUSTOM_ID    NVARCHAR2(255),
    ORIG_PROCESS_ID   NVARCHAR2(255),
    ORIG_HOSTNAME     NVARCHAR2(128),
    ORIG_IP           NVARCHAR2(64),
    ORIG_PRINCIPAL    NVARCHAR2(255),
    MI_PORT_TYPE      NVARCHAR2(255),
    MI_OPERATION_NAME NVARCHAR2(255),
    MI_MESSAGE_ID     NVARCHAR2(255),
    MI_FLOW_ID        NVARCHAR2(64),
    MI_TRANSPORT_TYPE NVARCHAR2(255),
    MESSAGE_CONTENT   CLOB,
    SERVICE_KEY       VARCHAR2(255) DEFAULT NULL,
    HTTP_METHOD       VARCHAR2(100) DEFAULT NULL,
    URI               CLOB   DEFAULT NULL,
    QUERYSTRING       CLOB,
    PROTOCOL          VARCHAR2(100) DEFAULT NULL,
    HTTP_HEADERS      CLOB,
    CONSUMER_IP       VARCHAR2(255) DEFAULT NULL,
    HTTP_STATUS       NUMBER DEFAULT NULL,
    RESPONSE_TIME     NUMBER DEFAULT NULL,
    FAILURE_CAUSE     VARCHAR2(255) DEFAULT NULL,
    MESSAGE_TYPE      VARCHAR2(255) DEFAULT NULL,
    CONTENT_CUT       NUMBER(1),
    PRIMARY KEY (ID)
);

CREATE TABLE EVENTS_CUSTOMINFO
(
    ID         NUMBER(19) NOT NULL,
    EVENT_ID   NUMBER(19) NOT NULL,
    CUST_KEY   NVARCHAR2(255),
    CUST_VALUE NVARCHAR2(255),
    PRIMARY KEY (ID)
);

-- 访问控制规则定义表
CREATE TABLE access_control_ip
(
    id          VARCHAR2(100) NOT NULL,
    service_key VARCHAR2(255) DEFAULT NULL,
    type        VARCHAR2(100) DEFAULT NULL,
    black_list  CLOB,
    white_list  CLOB,
    status      CHAR(1)   DEFAULT '1',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE access_control_flow
(
    id                 VARCHAR2(100) NOT NULL,
    service_key        VARCHAR2(255) DEFAULT NULL,
    type               VARCHAR2(100) DEFAULT NULL,
    time_interval      NUMBER,
    interval_threshold NUMBER,
    single_threshold   NUMBER,
    status             CHAR(1)   DEFAULT '1',
    create_time        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE access_control_frequency
(
    id            VARCHAR2(100) NOT NULL,
    service_key   VARCHAR2(255) DEFAULT NULL,
    type          VARCHAR2(100) DEFAULT NULL,
    time_interval NUMBER,
    threshold     NUMBER,
    status        CHAR(1)   DEFAULT '1',
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- flow_meter_catcher

CREATE TABLE flow_meter_catcher
(
    moment            DATE   DEFAULT NULL,
    uuid              VARCHAR2(50) DEFAULT NULL,
    pid               VARCHAR2(20) DEFAULT NULL,
    father_pid        VARCHAR2(20) DEFAULT NULL,
    root_pid          VARCHAR2(20) DEFAULT NULL,
    system_pid        NUMBER DEFAULT NULL,
    project           VARCHAR2(50) DEFAULT NULL,
    job               VARCHAR2(255) DEFAULT NULL,
    job_repository_id VARCHAR2(255) DEFAULT NULL,
    job_version       VARCHAR2(255) DEFAULT NULL,
    context           VARCHAR2(50) DEFAULT NULL,
    origin            VARCHAR2(255) DEFAULT NULL,
    label             VARCHAR2(255) DEFAULT NULL,
    count             NUMBER DEFAULT NULL,
    reference         NUMBER DEFAULT NULL,
    thresholds        VARCHAR2(255) DEFAULT NULL
);

-- log_catcher

CREATE TABLE log_catcher
(
    moment     DATE   DEFAULT NULL,
    uuid       VARCHAR2(50) DEFAULT NULL,
    pid        VARCHAR2(20) DEFAULT NULL,
    root_pid   VARCHAR2(20) DEFAULT NULL,
    father_pid VARCHAR2(20) DEFAULT NULL,
    project    VARCHAR2(50) DEFAULT NULL,
    job        VARCHAR2(255) DEFAULT NULL,
    context    VARCHAR2(50) DEFAULT NULL,
    priority   NUMBER DEFAULT NULL,
    type       VARCHAR2(255) DEFAULT NULL,
    origin     VARCHAR2(255) DEFAULT NULL,
    message    VARCHAR2(255) DEFAULT NULL,
    code       NUMBER DEFAULT NULL
);

-- stat_catcher

CREATE TABLE stat_catcher
(
    moment            DATE   DEFAULT NULL,
    uuid              VARCHAR2(50) DEFAULT NULL,
    pid               VARCHAR2(20) DEFAULT NULL,
    father_pid        VARCHAR2(20) DEFAULT NULL,
    root_pid          VARCHAR2(20) DEFAULT NULL,
    system_pid        NUMBER DEFAULT NULL,
    project           VARCHAR2(50) DEFAULT NULL,
    job               VARCHAR2(255) DEFAULT NULL,
    job_repository_id VARCHAR2(255) DEFAULT NULL,
    job_version       VARCHAR2(255) DEFAULT NULL,
    context           VARCHAR2(50) DEFAULT NULL,
    origin            VARCHAR2(255) DEFAULT NULL,
    message_type      VARCHAR2(255) DEFAULT NULL,
    message           VARCHAR2(255) DEFAULT NULL,
    duration          NUMBER DEFAULT NULL
);

COMMIT;
