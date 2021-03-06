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
package com.blackducksoftware.integration.hub.alert.channel.hipchat;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackducksoftware.integration.hub.alert.channel.SupportedChannels;
import com.blackducksoftware.integration.hub.alert.channel.hipchat.controller.distribution.HipChatDistributionRestModel;
import com.blackducksoftware.integration.hub.alert.channel.hipchat.repository.distribution.HipChatDistributionConfigEntity;
import com.blackducksoftware.integration.hub.alert.channel.hipchat.repository.distribution.HipChatDistributionRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.channel.hipchat.repository.global.GlobalHipChatConfigEntity;
import com.blackducksoftware.integration.hub.alert.channel.hipchat.repository.global.GlobalHipChatRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.channel.manager.DistributionChannelManager;
import com.blackducksoftware.integration.hub.alert.digest.model.ProjectData;
import com.blackducksoftware.integration.hub.alert.exception.AlertException;
import com.blackducksoftware.integration.hub.alert.web.ObjectTransformer;

@Component
public class HipChatManager extends DistributionChannelManager<GlobalHipChatConfigEntity, HipChatDistributionConfigEntity, HipChatEvent, HipChatDistributionRestModel> {
    @Autowired
    public HipChatManager(final HipChatChannel distributionChannel, final GlobalHipChatRepositoryWrapper globalRepository, final HipChatDistributionRepositoryWrapper localRepository, final ObjectTransformer objectTransformer) {
        super(distributionChannel, globalRepository, localRepository, objectTransformer);
    }

    @Override
    public boolean isApplicable(final String supportedChannelName) {
        return SupportedChannels.HIPCHAT.equals(supportedChannelName);
    }

    @Override
    public HipChatEvent createChannelEvent(final Collection<ProjectData> projectDataCollection, final Long commonDistributionConfigId) {
        return new HipChatEvent(projectDataCollection, commonDistributionConfigId);
    }

    @Override
    public String sendTestMessage(final HipChatDistributionRestModel restModel) throws AlertException {
        if (getDistributionChannel().getGlobalConfigEntity() != null) {
            return super.sendTestMessage(restModel);
        }
        return "ERROR: Missing global configuration!";
    }

    @Override
    public Class<HipChatDistributionConfigEntity> getDatabaseEntityClass() {
        return HipChatDistributionConfigEntity.class;
    }

}
