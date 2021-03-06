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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.blackducksoftware.integration.hub.alert.datasource.SimpleKeyRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.datasource.entity.DatabaseEntity;
import com.blackducksoftware.integration.hub.alert.exception.AlertException;
import com.blackducksoftware.integration.hub.alert.mock.entity.global.MockGlobalEntityUtil;
import com.blackducksoftware.integration.hub.alert.mock.model.global.MockGlobalRestModelUtil;
import com.blackducksoftware.integration.hub.alert.web.ObjectTransformer;
import com.blackducksoftware.integration.hub.alert.web.actions.ConfigActions;
import com.blackducksoftware.integration.hub.alert.web.model.ConfigRestModel;

public abstract class GlobalActionsTest<GR extends ConfigRestModel, GE extends DatabaseEntity, GW extends SimpleKeyRepositoryWrapper<GE, ?>, GCA extends ConfigActions<GE, GR, GW>> {
    protected GCA configActions;

    public GlobalActionsTest() {
        configActions = getMockedConfigActions();
    }

    public abstract MockGlobalEntityUtil<GE> getGlobalEntityMockUtil();

    public abstract MockGlobalRestModelUtil<GR> getGlobalRestModelMockUtil();

    public abstract GCA getMockedConfigActions();

    @Test
    public void testDoesConfigExist() {

        Mockito.when(configActions.getRepository().exists(Mockito.anyLong())).thenReturn(true);
        assertTrue(configActions.doesConfigExist(1L));
        assertTrue(configActions.doesConfigExist("1"));

        Mockito.when(configActions.getRepository().exists(Mockito.anyLong())).thenReturn(false);
        assertFalse(configActions.doesConfigExist(1L));
        assertFalse(configActions.doesConfigExist("1"));

        final String idString = null;
        final Long idLong = null;
        assertFalse(configActions.doesConfigExist(idString));
        assertFalse(configActions.doesConfigExist(idLong));
    }

    @Test
    public void testGetConfig() throws Exception {
        Mockito.when(configActions.getRepository().findOne(Mockito.anyLong())).thenReturn(getGlobalEntityMockUtil().createGlobalEntity());
        Mockito.when(configActions.getRepository().findAll()).thenReturn(Arrays.asList(getGlobalEntityMockUtil().createGlobalEntity()));

        // We must mask the rest model because the configActions will have masked those returned by getConfig(...)
        final GR restModel = getGlobalRestModelMockUtil().createGlobalRestModel();
        configActions.maskRestModel(restModel);

        List<GR> configsById = configActions.getConfig(1L);
        List<GR> allConfigs = configActions.getConfig(null);

        assertTrue(configsById.size() == 1);
        assertTrue(allConfigs.size() == 1);

        final GR configById = configsById.get(0);
        final GR config = allConfigs.get(0);
        assertEquals(restModel, configById);
        assertEquals(restModel, config);

        Mockito.when(configActions.getRepository().findOne(Mockito.anyLong())).thenReturn(null);
        Mockito.when(configActions.getRepository().findAll()).thenReturn(null);

        configsById = configActions.getConfig(1L);
        allConfigs = configActions.getConfig(null);

        assertNotNull(configsById);
        assertNotNull(allConfigs);
        assertTrue(configsById.isEmpty());
        assertTrue(allConfigs.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteConfig() {
        configActions.deleteConfig(1L);

        verify(configActions.getRepository(), times(1)).delete(Mockito.anyLong());

        Mockito.reset(configActions.getRepository());
        configActions.deleteConfig("1");

        verify(configActions.getRepository(), times(1)).delete(Mockito.anyLong());

        final String idString = null;
        final Long idLong = null;

        Mockito.reset(configActions.getRepository());

        configActions.deleteConfig(idLong);
        verify(configActions.getRepository(), times(0)).delete(Mockito.anyLong());

        Mockito.reset(configActions.getRepository());

        configActions.deleteConfig(idString);
        verify(configActions.getRepository(), times(0)).delete(Mockito.anyLong());

    }

    @Test
    public void testSaveConfig() throws Exception {
        final GE expectedHipChatConfigEntity = getGlobalEntityMockUtil().createGlobalEntity();
        Mockito.when(configActions.getRepository().save(Mockito.any(getGlobalEntityClass()))).thenReturn(expectedHipChatConfigEntity);

        GE emailConfigEntity = configActions.saveConfig(getGlobalRestModelMockUtil().createGlobalRestModel());
        assertNotNull(emailConfigEntity);
        assertEquals(expectedHipChatConfigEntity, emailConfigEntity);

        emailConfigEntity = configActions.saveConfig(null);
        assertNull(emailConfigEntity);

        Mockito.when(configActions.getRepository().save(Mockito.any(getGlobalEntityClass()))).thenThrow(new RuntimeException("test"));
        try {
            emailConfigEntity = configActions.saveConfig(getGlobalRestModelMockUtil().createGlobalRestModel());
            fail();
        } catch (final AlertException e) {
            assertEquals("test", e.getMessage());
        }

        final ObjectTransformer transformer = Mockito.mock(ObjectTransformer.class);
        Mockito.when(transformer.configRestModelToDatabaseEntity(Mockito.any(), Mockito.any())).thenReturn(null);
        configActions = createMockedConfigActionsUsingObjectTransformer(transformer);

        emailConfigEntity = configActions.saveConfig(getGlobalRestModelMockUtil().createGlobalRestModel());
        assertNull(emailConfigEntity);
    }

    public abstract GCA createMockedConfigActionsUsingObjectTransformer(ObjectTransformer objectTransformer);

    public abstract Class<GE> getGlobalEntityClass();

    @Test
    public abstract void testConfigurationChangeTriggers();

    @Test
    public void testIsBoolean() {
        assertFalse(configActions.isBoolean(null));
        assertFalse(configActions.isBoolean(""));
        assertFalse(configActions.isBoolean("string"));
        assertFalse(configActions.isBoolean(" cat"));
        assertTrue(configActions.isBoolean("true"));
        assertTrue(configActions.isBoolean("false"));
        assertTrue(configActions.isBoolean("TRUE"));
        assertTrue(configActions.isBoolean("FALSE"));
        assertTrue(configActions.isBoolean("  TruE"));
        assertTrue(configActions.isBoolean("FaLSE  "));
    }

    @Test
    public abstract void testInvalidConfig();

    @Test
    public void testValidConfig() throws Exception {
        final GR restModel = getGlobalRestModelMockUtil().createGlobalRestModel();
        final String result = configActions.validateConfig(restModel);

        assertEquals("Valid", result);
    }

    @Test
    public void testChannelTestConfig() throws Exception {
        final String actual = configActions.channelTestConfig(null);

        assertNull(actual);
    }

}
