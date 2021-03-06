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
package com.blackducksoftware.integration.hub.alert.audit.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.alert.audit.repository.relation.AuditNotificationRelation;
import com.blackducksoftware.integration.hub.alert.audit.repository.relation.AuditNotificationRelationPK;
import com.blackducksoftware.integration.hub.alert.datasource.AbstractRepositoryWrapper;

@Component
public class AuditNotificationRepositoryWrapper extends AbstractRepositoryWrapper<AuditNotificationRelation, AuditNotificationRelationPK, AuditNotificationRepository> {

    public AuditNotificationRepositoryWrapper(final AuditNotificationRepository repository) {
        super(repository);
    }

    public List<AuditNotificationRelation> findByAuditEntryId(final Long auditEntryId) {
        return decryptSensitiveData(getRepository().findByAuditEntryId(auditEntryId));
    }

    public List<AuditNotificationRelation> findByNotificationId(final Long notificationId) {
        return decryptSensitiveData(getRepository().findByNotificationId(notificationId));
    }

    @Override
    public AuditNotificationRelation encryptSensitiveData(final AuditNotificationRelation entity) throws EncryptionException {
        return entity;
    }

    @Override
    public AuditNotificationRelation decryptSensitiveData(final AuditNotificationRelation entity) throws EncryptionException {
        return entity;
    }
}
