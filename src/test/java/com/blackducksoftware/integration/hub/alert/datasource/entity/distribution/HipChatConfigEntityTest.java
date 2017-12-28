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
package com.blackducksoftware.integration.hub.alert.datasource.entity.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.blackducksoftware.integration.hub.alert.mock.entity.MockEntityUtil;
import com.blackducksoftware.integration.hub.alert.mock.entity.MockHipChatEntity;

public class HipChatConfigEntityTest extends EntityTest<HipChatDistributionConfigEntity> {
    private final MockHipChatEntity mockUtils = new MockHipChatEntity();

    @Override
    public MockEntityUtil<HipChatDistributionConfigEntity> getMockUtil() {
        return mockUtils;
    }

    @Override
    public Class<HipChatDistributionConfigEntity> getEntityClass() {
        return HipChatDistributionConfigEntity.class;
    }

    @Override
    public void assertEntityFieldsNull(final HipChatDistributionConfigEntity entity) {
        assertNull(entity.getRoomId());
        assertNull(entity.getColor());
        assertNull(entity.getNotify());
    }

    @Override
    public long entitySerialId() {
        return HipChatDistributionConfigEntity.getSerialversionuid();
    }

    @Override
    public int emptyEntityHashCode() {
        return 31860737;
    }

    @Override
    public void assertEntityFieldsFull(final HipChatDistributionConfigEntity entity) {
        assertEquals(mockUtils.getRoomId(), entity.getRoomId());
        assertEquals(mockUtils.isNotify(), entity.getNotify());
        assertEquals(mockUtils.getColor(), entity.getColor());
    }

    @Override
    public int entityHashCode() {
        return -789557399;
    }

}
