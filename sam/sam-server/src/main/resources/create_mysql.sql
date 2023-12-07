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
    EI_TIMESTAMP      DATETIME,
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
    MESSAGE_CONTENT   LONGTEXT,
    SERVICE_KEY       VARCHAR(255) DEFAULT NULL COMMENT '服务接口的唯一标识，目前使用的时serviceaddress作为其值',
    HTTP_METHOD       VARCHAR(100) DEFAULT NULL COMMENT 'http请求方法，http请求行中的内容信息之一',
    URI               LONGTEXT     DEFAULT NULL COMMENT 'URI，http请求行中的内容信息之一',
    QUERYSTRING       LONGTEXT     DEFAULT NULL COMMENT '参数字符串，http请求行中的内容信息之一',
    PROTOCOL          VARCHAR(100) DEFAULT NULL COMMENT '协议，http请求行中的内容信息之一',
    HTTP_HEADERS      LONGTEXT     DEFAULT NULL COMMENT 'http的请求和响应头部信息（header）',
    CONSUMER_IP       VARCHAR(255) DEFAULT NULL COMMENT '消费方IP，即请求该服务接口的客户端IP地址，目前只能是IPV4的地址（IPV6的地址也能记录，但是可能存在一些未知问题）',
    HTTP_STATUS       INT          DEFAULT NULL COMMENT 'http状态码，访问控制功能新增',
    RESPONSE_TIME     BIGINT       DEFAULT NULL COMMENT '响应时间，一个服务接口从请求至相应之间的耗时，以毫秒为单位',
    FAILURE_CAUSE     VARCHAR(255) DEFAULT NULL COMMENT '失败原因，若此次请求为非正常（失败）响应，则记录错误相应信息（失败响应的原因）',
    MESSAGE_TYPE      VARCHAR(255) DEFAULT NULL COMMENT '报文的类型，用于标志此条报文日志是request（REQ）还是response（RESP）',
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

-- 访问控制规则定义表
CREATE TABLE access_control_ip
(
    id          VARCHAR(100) NOT NULL COMMENT '主键ID，UUID',
    service_key VARCHAR(255)          DEFAULT NULL COMMENT '服务（webservice接口）的唯一标识，目前的取值为serviceaddress',
    type        VARCHAR(100)          DEFAULT NULL COMMENT '启用类型 两种情况：\r\n启用黑名单控制时，值为 "black"\r\n启用白名单控制时，值为 "white"\r\n注意：不存在同时启动黑名单和白名单的情况',
    black_list  LONGTEXT COMMENT '黑名单列表',
    white_list  LONGTEXT COMMENT '白名单列表',
    status      char(1)      NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    create_time DATETIME     NOT NULL COMMENT '创建时间',
    update_time DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT ='访问控制功能IP控制规则表';

CREATE TABLE access_control_flow
(
    id                 VARCHAR(100) NOT NULL COMMENT '主键ID，UUID',
    service_key        VARCHAR(255)          DEFAULT NULL COMMENT '服务（webservice接口）的唯一标识，目前的取值为serviceaddress',
    type               VARCHAR(100)          DEFAULT NULL COMMENT '启用类型 三种情况：\r\n一、值为 "single"\r\n启动单次流量控制，即对单次请求的流量大小进行控制。\r\n二、值为 "global"\r\n启用区间-全局流量控制，且是全局，即流量的控制是针对服务而言。\r\n三、值为 "consumer"\r\n启用区间-消费方流量控制，即流量的控制在针对服务的基础上，细分消费者，消费者的差异根据消费方IP区分。\r\n注意：不存在同时启用以上两种类型以上的情况，有且仅有以上情况中的一种',
    time_interval      BIGINT                DEFAULT NULL COMMENT '定义流量控制规则的时间区间，其秒为单位',
    interval_threshold BIGINT                DEFAULT NULL COMMENT '定义流量控制规则的阈值，即在规定的时间区间内允许访问的最大流量阈值',
    single_threshold   BIGINT                DEFAULT NULL COMMENT '定义流量控制规则的单次流量阈值',
    status             char(1)      NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    create_time        DATETIME     NOT NULL COMMENT '创建时间',
    update_time        DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT ='访问控制功能流量控制规则表';

CREATE TABLE access_control_frequency
(
    id            VARCHAR(100) NOT NULL COMMENT '主键ID，UUID',
    service_key   VARCHAR(255)          DEFAULT NULL COMMENT '服务（webservice接口）的唯一标识，目前的取值为serviceaddress',
    type          VARCHAR(100)          DEFAULT NULL COMMENT '启用类型 两种情况：\r\n一、值为 "global"\r\n启用全局控制，即频次的控制是针对服务而言。\r\n二、值为 "consumer"\r\n启用消费方控制，即频次的控制在针对服务的基础上，细分消费者，消费者的差异根据消费方IP区分。\r\n注意：不存在同时启用以上两种类型以上的情况，有且仅有以上情况中的一种',
    time_interval BIGINT                DEFAULT NULL COMMENT '定义频率规则的时间区间，其秒为单位',
    threshold     BIGINT                DEFAULT NULL COMMENT '定义频率规则的阈值，即在规定的时间区间内允许访问的最大次数',
    status        char(1)      NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    create_time   DATETIME     NOT NULL COMMENT '创建时间',
    update_time   DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT ='访问控制功能频次控制规则表';

-- flow_meter_catcher

CREATE TABLE flow_meter_catcher
(
    moment            datetime     DEFAULT NULL,
    uuid              varchar(50)  DEFAULT NULL,
    pid               varchar(20)  DEFAULT NULL,
    father_pid        varchar(20)  DEFAULT NULL,
    root_pid          varchar(20)  DEFAULT NULL,
    system_pid        bigint       DEFAULT NULL,
    project           varchar(50)  DEFAULT NULL,
    job               varchar(255) DEFAULT NULL,
    job_repository_id varchar(255) DEFAULT NULL,
    job_version       varchar(255) DEFAULT NULL,
    context           varchar(50)  DEFAULT NULL,
    origin            varchar(255) DEFAULT NULL,
    label             varchar(255) DEFAULT NULL,
    count             int          DEFAULT NULL,
    reference         int          DEFAULT NULL,
    thresholds        varchar(255) DEFAULT NULL
) COMMENT ='流量记录表';

-- log_catcher

CREATE TABLE log_catcher
(
    moment     datetime     DEFAULT NULL,
    uuid       varchar(50)  DEFAULT NULL,
    pid        varchar(20)  DEFAULT NULL,
    root_pid   varchar(20)  DEFAULT NULL,
    father_pid varchar(20)  DEFAULT NULL,
    project    varchar(50)  DEFAULT NULL,
    job        varchar(255) DEFAULT NULL,
    context    varchar(50)  DEFAULT NULL,
    priority   int          DEFAULT NULL,
    type       varchar(255) DEFAULT NULL,
    origin     varchar(255) DEFAULT NULL,
    message    varchar(255) DEFAULT NULL,
    code       int          DEFAULT NULL
) COMMENT ='日志记录表';

-- stat_catcher

CREATE TABLE stat_catcher
(
    moment            datetime     DEFAULT NULL,
    uuid              varchar(50)  DEFAULT NULL,
    pid               varchar(20)  DEFAULT NULL,
    father_pid        varchar(20)  DEFAULT NULL,
    root_pid          varchar(20)  DEFAULT NULL,
    system_pid        bigint       DEFAULT NULL,
    project           varchar(50)  DEFAULT NULL,
    job               varchar(255) DEFAULT NULL,
    job_repository_id varchar(255) DEFAULT NULL,
    job_version       varchar(255) DEFAULT NULL,
    context           varchar(50)  DEFAULT NULL,
    origin            varchar(255) DEFAULT NULL,
    message_type      varchar(255) DEFAULT NULL,
    message           varchar(255) DEFAULT NULL,
    duration          bigint       DEFAULT NULL
) COMMENT ='组件信息表';

CREATE TABLE SEQUENCE
(
    SEQ_NAME  VARCHAR(50) NOT NULL,
    SEQ_COUNT DECIMAL(38),
    PRIMARY KEY (SEQ_NAME)
);
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT)
values ('EVENT_SEQ', 0);
COMMIT;
