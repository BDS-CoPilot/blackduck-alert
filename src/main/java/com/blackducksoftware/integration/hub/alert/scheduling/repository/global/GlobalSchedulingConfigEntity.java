/**
 * hub-alert
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.alert.scheduling.repository.global;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.blackducksoftware.integration.hub.alert.datasource.entity.DatabaseEntity;

@Entity
@Table(schema = "alert", name = "global_scheduling_config")
public class GlobalSchedulingConfigEntity extends DatabaseEntity {
    @Column(name = "alert_digest_daily_cron")
    private String dailyDigestCron;

    @Column(name = "alert_purge_data_cron")
    private String purgeDataCron;

    public GlobalSchedulingConfigEntity() {

    }

    public GlobalSchedulingConfigEntity(final String dailyDigestCron, final String purgeDataCron) {
        this.dailyDigestCron = dailyDigestCron;
        this.purgeDataCron = purgeDataCron;
    }

    public String getDailyDigestCron() {
        return dailyDigestCron;
    }

    public String getPurgeDataCron() {
        return purgeDataCron;
    }

}
