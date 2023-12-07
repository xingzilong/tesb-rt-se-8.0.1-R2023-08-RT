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
CREATE TABLE EVENTS
(
    ID                BIGINT NOT NULL,
    EI_TIMESTAMP      TIMESTAMP,
    EI_EVENT_TYPE     VARCHAR(255),
    ORIG_CUSTOM_ID    VARCHAR(255),
    ORIG_PROCESS_ID   VARCHAR(255),
    ORIG_HOSTNAME     VARCHAR(128),
    ORIG_IP           VARCHAR(64),
    ORIG_PRINCIPAL    VARCHAR(255),
    MI_PORT_TYPE      VARCHAR(255),
    MI_OPERATION_NAME VARCHAR(255),
    MI_MESSAGE_ID     VARCHAR(255),
    MI_FLOW_ID        VARCHAR(64),
    MI_TRANSPORT_TYPE VARCHAR(255),
    MESSAGE_CONTENT   CLOB,
    SERVICE_KEY       VARCHAR(255),
    HTTP_METHOD       VARCHAR(100),
    URI               CLOB,
    QUERYSTRING       CLOB,
    PROTOCOL          VARCHAR(100),
    HTTP_HEADERS      CLOB,
    CONSUMER_IP       VARCHAR(255),
    HTTP_STATUS       INT,
    RESPONSE_TIME     BIGINT,
    FAILURE_CAUSE     VARCHAR(255),
    MESSAGE_TYPE      VARCHAR(255),
    CONTENT_CUT       BOOLEAN,
    PRIMARY KEY (ID)
);

CREATE TABLE EVENTS_CUSTOMINFO
(
    ID         BIGINT NOT NULL,
    EVENT_ID   BIGINT NOT NULL,
    CUST_KEY   VARCHAR(255),
    CUST_VALUE VARCHAR(255),
    PRIMARY KEY (ID)
);

CREATE TABLE TAB_SEQUENCE
(
    ID_VALUE int generated always as identity,
    DUMMY    char(1)
);
INSERT INTO TAB_SEQUENCE (DUMMY)
values (null);

-- 访问控制规则定义表
CREATE TABLE access_control_ip
(
    id          VARCHAR(100) NOT NULL,
    service_key VARCHAR(255),
    type        VARCHAR(100),
    black_list  CLOB,
    white_list  CLOB,
    status      CHAR(1)      NOT NULL DEFAULT '1',
    create_time TIMESTAMP    NOT NULL,
    update_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE access_control_flow
(
    id                 VARCHAR(100) NOT NULL,
    service_key        VARCHAR(255),
    type               VARCHAR(100),
    time_interval      BIGINT,
    interval_threshold BIGINT,
    single_threshold   BIGINT,
    status             CHAR(1)      NOT NULL DEFAULT '1',
    create_time        TIMESTAMP    NOT NULL,
    update_time        TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE access_control_frequency
(
    id            VARCHAR(100) NOT NULL,
    service_key   VARCHAR(255),
    type          VARCHAR(100),
    time_interval BIGINT,
    threshold     BIGINT,
    status        CHAR(1)      NOT NULL DEFAULT '1',
    create_time   TIMESTAMP    NOT NULL,
    update_time   TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

-- flow_meter_catcher

CREATE TABLE flow_meter_catcher
(
    moment            TIMESTAMP    DEFAULT NULL,
    uuid              VARCHAR(50)  DEFAULT NULL,
    pid               VARCHAR(20)  DEFAULT NULL,
    father_pid        VARCHAR(20)  DEFAULT NULL,
    root_pid          VARCHAR(20)  DEFAULT NULL,
    system_pid        BIGINT       DEFAULT NULL,
    project           VARCHAR(50)  DEFAULT NULL,
    job               VARCHAR(255) DEFAULT NULL,
    job_repository_id VARCHAR(255) DEFAULT NULL,
    job_version       VARCHAR(255) DEFAULT NULL,
    context           VARCHAR(50)  DEFAULT NULL,
    origin            VARCHAR(255) DEFAULT NULL,
    label             VARCHAR(255) DEFAULT NULL,
    count             INT          DEFAULT NULL,
    reference         INT          DEFAULT NULL,
    thresholds        VARCHAR(255) DEFAULT NULL
);

-- log_catcher

CREATE TABLE log_catcher
(
    moment     TIMESTAMP    DEFAULT NULL,
    uuid       VARCHAR(50)  DEFAULT NULL,
    pid        VARCHAR(20)  DEFAULT NULL,
    root_pid   VARCHAR(20)  DEFAULT NULL,
    father_pid VARCHAR(20)  DEFAULT NULL,
    project    VARCHAR(50)  DEFAULT NULL,
    job        VARCHAR(255) DEFAULT NULL,
    context    VARCHAR(50)  DEFAULT NULL,
    priority   INT          DEFAULT NULL,
    type       VARCHAR(255) DEFAULT NULL,
    origin     VARCHAR(255) DEFAULT NULL,
    message    VARCHAR(255) DEFAULT NULL,
    code       INT          DEFAULT NULL
);

-- stat_catcher

CREATE TABLE stat_catcher
(
    moment            TIMESTAMP    DEFAULT NULL,
    uuid              VARCHAR(50)  DEFAULT NULL,
    pid               VARCHAR(20)  DEFAULT NULL,
    father_pid        VARCHAR(20)  DEFAULT NULL,
    root_pid          VARCHAR(20)  DEFAULT NULL,
    system_pid        BIGINT       DEFAULT NULL,
    project           VARCHAR(50)  DEFAULT NULL,
    job               VARCHAR(255) DEFAULT NULL,
    job_repository_id VARCHAR(255) DEFAULT NULL,
    job_version       VARCHAR(255) DEFAULT NULL,
    context           VARCHAR(50)  DEFAULT NULL,
    origin            VARCHAR(255) DEFAULT NULL,
    message_type      VARCHAR(255) DEFAULT NULL,
    message           VARCHAR(255) DEFAULT NULL,
    duration          BIGINT       DEFAULT NULL
);
