/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.hub.alert.web.actions.global;

import org.mockito.Mockito;

import com.blackducksoftware.integration.hub.alert.datasource.entity.global.GlobalEmailConfigEntity;
import com.blackducksoftware.integration.hub.alert.datasource.entity.repository.global.GlobalEmailRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.mock.EmailMockUtils;
import com.blackducksoftware.integration.hub.alert.web.ObjectTransformer;
import com.blackducksoftware.integration.hub.alert.web.model.global.GlobalEmailConfigRestModel;

public class GlobalEmailConfigActionsTest extends GlobalActionsTest<GlobalEmailConfigRestModel, GlobalEmailConfigEntity, GlobalEmailRepositoryWrapper, GlobalEmailConfigActions> {
    private static EmailMockUtils mockUtils = new EmailMockUtils();

    public GlobalEmailConfigActionsTest() {
        super(mockUtils);
    }

    @Override
    public GlobalEmailConfigActions getMockedConfigActions() {
        final GlobalEmailRepositoryWrapper repository = Mockito.mock(GlobalEmailRepositoryWrapper.class);
        final ObjectTransformer objectTransformer = new ObjectTransformer();
        return new GlobalEmailConfigActions(repository, objectTransformer);
    }

    @Override
    public GlobalEmailConfigActions createMockedConfigActionsUsingObjectTransformer(final ObjectTransformer objectTransformer) {
        final GlobalEmailRepositoryWrapper repository = Mockito.mock(GlobalEmailRepositoryWrapper.class);
        return new GlobalEmailConfigActions(repository, objectTransformer);
    }

    @Override
    public Class<GlobalEmailConfigEntity> getGlobalEntityClass() {
        return GlobalEmailConfigEntity.class;
    }

    @Override
    public void testConfigurationChangeTriggers() {
    }

}