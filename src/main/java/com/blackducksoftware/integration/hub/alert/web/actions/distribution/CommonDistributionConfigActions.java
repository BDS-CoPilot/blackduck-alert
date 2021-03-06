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
package com.blackducksoftware.integration.hub.alert.web.actions.distribution;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.alert.audit.repository.AuditEntryEntity;
import com.blackducksoftware.integration.hub.alert.audit.repository.AuditEntryRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.audit.repository.AuditNotificationRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.audit.repository.relation.AuditNotificationRelation;
import com.blackducksoftware.integration.hub.alert.datasource.entity.CommonDistributionConfigEntity;
import com.blackducksoftware.integration.hub.alert.datasource.entity.repository.CommonDistributionRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.exception.AlertException;
import com.blackducksoftware.integration.hub.alert.exception.AlertFieldException;
import com.blackducksoftware.integration.hub.alert.web.ObjectTransformer;
import com.blackducksoftware.integration.hub.alert.web.actions.ConfiguredProjectsActions;
import com.blackducksoftware.integration.hub.alert.web.actions.NotificationTypesActions;
import com.blackducksoftware.integration.hub.alert.web.model.distribution.CommonDistributionConfigRestModel;

@Component
public class CommonDistributionConfigActions extends DistributionConfigActions<CommonDistributionConfigEntity, CommonDistributionConfigRestModel, CommonDistributionRepositoryWrapper> {
    private final AuditEntryRepositoryWrapper auditEntryRepository;
    private final AuditNotificationRepositoryWrapper auditNotificationRepository;

    @Autowired
    public CommonDistributionConfigActions(final CommonDistributionRepositoryWrapper commonDistributionRepository, final AuditEntryRepositoryWrapper auditEntryRepository,
            final ConfiguredProjectsActions<CommonDistributionConfigRestModel> configuredProjectsActions, final NotificationTypesActions<CommonDistributionConfigRestModel> notificationTypesActions,
            final ObjectTransformer objectTransformer, final AuditNotificationRepositoryWrapper auditNotificationRepository) {
        super(CommonDistributionConfigEntity.class, CommonDistributionConfigRestModel.class, commonDistributionRepository, commonDistributionRepository, configuredProjectsActions, notificationTypesActions, objectTransformer);
        this.auditEntryRepository = auditEntryRepository;
        this.auditNotificationRepository = auditNotificationRepository;
    }

    @Override
    public List<CommonDistributionConfigRestModel> getConfig(final Long id) throws AlertException {
        final List<CommonDistributionConfigRestModel> restModels = super.getConfig(id);
        addAuditEntryInfoToRestModels(restModels);
        return restModels;
    }

    private void addAuditEntryInfoToRestModels(final List<CommonDistributionConfigRestModel> restModels) {
        for (final CommonDistributionConfigRestModel restModel : restModels) {
            addAuditEntryInfoToRestModel(restModel);
        }
    }

    private void addAuditEntryInfoToRestModel(final CommonDistributionConfigRestModel restModel) {
        String lastRan = "Unknown";
        String status = "Unknown";
        final Long id = getObjectTransformer().stringToLong(restModel.getId());
        final AuditEntryEntity lastRanEntry = auditEntryRepository.findFirstByCommonConfigIdOrderByTimeLastSentDesc(id);
        if (lastRanEntry != null) {
            lastRan = getObjectTransformer().objectToString(lastRanEntry.getTimeLastSent());
            status = lastRanEntry.getStatus().getDisplayName();
        }
        restModel.setLastRan(lastRan);
        restModel.setStatus(status);
    }

    @Override
    public CommonDistributionConfigEntity saveConfig(final CommonDistributionConfigRestModel restModel) throws AlertException {
        if (restModel != null) {
            try {
                CommonDistributionConfigEntity createdEntity = getObjectTransformer().configRestModelToDatabaseEntity(restModel, getDatabaseEntityClass());
                if (createdEntity != null) {
                    createdEntity = getCommonDistributionRepository().save(createdEntity);
                    getConfiguredProjectsActions().saveConfiguredProjects(createdEntity, restModel);
                    getNotificationTypesActions().saveNotificationTypes(createdEntity, restModel);
                    return createdEntity;
                }
            } catch (final Exception e) {
                throw new AlertException(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public void deleteConfig(final Long id) {
        if (id != null) {
            deleteAuditEntries(id);
            getCommonDistributionRepository().delete(id);
            getConfiguredProjectsActions().cleanUpConfiguredProjects();
            getNotificationTypesActions().removeOldNotificationTypes(id);
        }
    }

    @Override
    public String channelTestConfig(final CommonDistributionConfigRestModel restModel) throws IntegrationException {
        // Should not be tested
        return "Configuration should not be tested at this level.";
    }

    @Override
    public CommonDistributionConfigRestModel constructRestModel(final CommonDistributionConfigEntity entity) throws AlertException {
        final CommonDistributionConfigEntity foundEntity = getCommonDistributionRepository().findOne(entity.getId());
        if (foundEntity != null) {
            return constructRestModel(foundEntity, null);
        }
        return null;
    }

    @Override
    public CommonDistributionConfigRestModel constructRestModel(final CommonDistributionConfigEntity commonEntity, final CommonDistributionConfigEntity distributionEntity) throws AlertException {
        final CommonDistributionConfigRestModel restModel = getObjectTransformer().databaseEntityToConfigRestModel(commonEntity, CommonDistributionConfigRestModel.class);
        restModel.setConfiguredProjects(getConfiguredProjectsActions().getConfiguredProjects(commonEntity));
        restModel.setNotificationTypes(getNotificationTypesActions().getNotificationTypes(commonEntity));
        return restModel;
    }

    @Override
    public String getDistributionName() {
        // This does not have a distribution name
        return null;
    }

    private void deleteAuditEntries(final Long configID) {
        final List<AuditEntryEntity> auditEntryList = auditEntryRepository.findByCommonConfigId(configID);
        auditEntryList.forEach((auditEntry) -> {
            final List<AuditNotificationRelation> relationList = auditNotificationRepository.findByAuditEntryId(auditEntry.getId());
            auditNotificationRepository.delete(relationList);
        });
        auditEntryRepository.delete(auditEntryList);
    }

    @Override
    public void validateDistributionConfig(final CommonDistributionConfigRestModel restModel, final Map<String, String> fieldErrors) throws AlertFieldException {
        // This does not validate anything
    }
}
