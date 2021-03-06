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
package com.blackducksoftware.integration.hub.alert.channel.hipchat.controller.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.blackducksoftware.integration.hub.alert.channel.hipchat.mock.MockHipChatRestModel;
import com.blackducksoftware.integration.hub.alert.web.model.CommonDistributionRestModelTest;

public class HipChatConfigRestModelTest extends CommonDistributionRestModelTest<HipChatDistributionRestModel> {

    @Override
    public void assertRestModelFieldsNull(final HipChatDistributionRestModel restModel) {
        assertNull(restModel.getRoomId());
        assertFalse(restModel.getNotify());
        assertNull(restModel.getColor());
    }

    @Override
    public void assertRestModelFieldsFull(final HipChatDistributionRestModel restModel) {
        assertEquals(getMockUtil().getRoomId(), restModel.getRoomId());
        assertEquals(getMockUtil().getNotify(), restModel.getNotify());
        assertEquals(getMockUtil().getColor(), restModel.getColor());
    }

    @Override
    public Class<HipChatDistributionRestModel> getRestModelClass() {
        return HipChatDistributionRestModel.class;
    }

    @Override
    public MockHipChatRestModel getMockUtil() {
        return new MockHipChatRestModel();
    }

}
