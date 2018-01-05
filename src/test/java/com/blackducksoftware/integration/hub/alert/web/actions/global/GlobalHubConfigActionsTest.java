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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.blackducksoftware.integration.hub.alert.TestGlobalProperties;
import com.blackducksoftware.integration.hub.alert.config.GlobalProperties;
import com.blackducksoftware.integration.hub.alert.datasource.entity.global.GlobalHubConfigEntity;
import com.blackducksoftware.integration.hub.alert.datasource.entity.repository.global.GlobalHubRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.datasource.entity.repository.global.GlobalSchedulingRepositoryWrapper;
import com.blackducksoftware.integration.hub.alert.exception.AlertFieldException;
import com.blackducksoftware.integration.hub.alert.mock.GlobalHubMockUtils;
import com.blackducksoftware.integration.hub.alert.web.ObjectTransformer;
import com.blackducksoftware.integration.hub.alert.web.model.global.GlobalHubConfigRestModel;
import com.blackducksoftware.integration.hub.builder.HubServerConfigBuilder;
import com.blackducksoftware.integration.hub.global.HubServerConfig;
import com.blackducksoftware.integration.hub.proxy.ProxyInfo;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.validator.HubServerConfigValidator;
import com.blackducksoftware.integration.validator.ValidationResults;

public class GlobalHubConfigActionsTest extends GlobalActionsTest<GlobalHubConfigRestModel, GlobalHubConfigEntity, GlobalHubRepositoryWrapper, GlobalHubConfigActions> {
    private static final GlobalHubMockUtils mockUtils = new GlobalHubMockUtils();

    public GlobalHubConfigActionsTest() {
        super(mockUtils);
    }

    @Override
    public GlobalHubConfigActions getMockedConfigActions() {
        final GlobalHubMockUtils mockUtils = new GlobalHubMockUtils();
        final GlobalHubRepositoryWrapper mockedGlobalRepository = Mockito.mock(GlobalHubRepositoryWrapper.class);
        final GlobalSchedulingRepositoryWrapper globalSchedulingRepository = Mockito.mock(GlobalSchedulingRepositoryWrapper.class);
        final GlobalProperties globalProperties = mockUtils.createTestGlobalProperties(mockedGlobalRepository, globalSchedulingRepository);

        final GlobalHubConfigActions configActions = new GlobalHubConfigActions(mockedGlobalRepository, globalProperties, new ObjectTransformer());
        return configActions;
    }

    @Override
    public GlobalHubConfigActions createMockedConfigActionsUsingObjectTransformer(final ObjectTransformer objectTransformer) {
        final GlobalHubMockUtils mockUtils = new GlobalHubMockUtils();
        final GlobalHubRepositoryWrapper mockedGlobalRepository = Mockito.mock(GlobalHubRepositoryWrapper.class);
        final GlobalSchedulingRepositoryWrapper globalSchedulingRepository = Mockito.mock(GlobalSchedulingRepositoryWrapper.class);
        final GlobalProperties globalProperties = mockUtils.createTestGlobalProperties(mockedGlobalRepository, globalSchedulingRepository);

        final GlobalHubConfigActions configActions = new GlobalHubConfigActions(mockedGlobalRepository, globalProperties, objectTransformer);
        return configActions;
    }

    @Override
    public Class<GlobalHubConfigEntity> getGlobalEntityClass() {
        return GlobalHubConfigEntity.class;
    }

    @Test
    @Override
    public void testGetConfig() throws Exception {
        final GlobalHubRepositoryWrapper mockedGlobalRepository = Mockito.mock(GlobalHubRepositoryWrapper.class);
        Mockito.when(mockedGlobalRepository.findOne(Mockito.anyLong())).thenReturn(mockUtils.createGlobalEntity());
        Mockito.when(mockedGlobalRepository.findAll()).thenReturn(Arrays.asList(mockUtils.createGlobalEntity()));
        final GlobalSchedulingRepositoryWrapper schedulingRepository = Mockito.mock(GlobalSchedulingRepositoryWrapper.class);
        final GlobalProperties globalProperties = mockUtils.createTestGlobalProperties(mockedGlobalRepository, schedulingRepository);
        final GlobalHubConfigRestModel defaultRestModel = mockUtils.createGlobalRestModel();

        final GlobalHubConfigActions configActions = new GlobalHubConfigActions(mockedGlobalRepository, globalProperties, new ObjectTransformer());
        final GlobalHubConfigRestModel maskedRestModel = configActions.maskRestModel(defaultRestModel);
        List<GlobalHubConfigRestModel> globalConfigsById = configActions.getConfig(1L);
        List<GlobalHubConfigRestModel> allGlobalConfigs = configActions.getConfig(null);

        assertTrue(globalConfigsById.size() == 1);
        assertTrue(allGlobalConfigs.size() == 1);

        final GlobalHubConfigRestModel globalConfigById = globalConfigsById.get(0);
        final GlobalHubConfigRestModel globalConfig = allGlobalConfigs.get(0);

        System.out.println(maskedRestModel.toString());
        System.out.println(globalConfigById.toString());

        assertEquals(maskedRestModel, globalConfigById);
        assertEquals(maskedRestModel, globalConfig);

        Mockito.when(mockedGlobalRepository.findOne(Mockito.anyLong())).thenReturn(null);
        Mockito.when(mockedGlobalRepository.findAll()).thenReturn(null);

        globalConfigsById = configActions.getConfig(1L);
        allGlobalConfigs = configActions.getConfig(null);

        assertNotNull(globalConfigsById);
        assertNotNull(allGlobalConfigs);
        assertTrue(globalConfigsById.isEmpty());
        assertTrue(allGlobalConfigs.size() == 1);

        final GlobalHubConfigRestModel environmentGlobalConfig = allGlobalConfigs.get(0);
        assertEquals(mockUtils.getHubAlwaysTrustCertificate(), environmentGlobalConfig.getHubAlwaysTrustCertificate());
        assertNull(environmentGlobalConfig.getHubApiKey());
        assertEquals(mockUtils.getHubProxyHost(), environmentGlobalConfig.getHubProxyHost());
        assertNull(environmentGlobalConfig.getHubProxyPassword());
        assertEquals(mockUtils.getHubProxyPort(), environmentGlobalConfig.getHubProxyPort());
        assertEquals(mockUtils.getHubProxyUsername(), environmentGlobalConfig.getHubProxyUsername());
        assertNull(environmentGlobalConfig.getHubTimeout());
        assertEquals(mockUtils.getHubUrl(), environmentGlobalConfig.getHubUrl());
        assertNull(environmentGlobalConfig.getId());

    }

    // TODO This will need to be fixed we modify the globalhubrestmodel proxy password
    // @Test
    public void testTestConfig() throws Exception {
        final GlobalHubMockUtils mockUtils = new GlobalHubMockUtils();
        final RestConnection mockedRestConnection = Mockito.mock(RestConnection.class);
        final GlobalHubRepositoryWrapper mockedGlobalRepository = Mockito.mock(GlobalHubRepositoryWrapper.class);
        final GlobalSchedulingRepositoryWrapper mockedSchedulingRepository = Mockito.mock(GlobalSchedulingRepositoryWrapper.class);
        final TestGlobalProperties globalProperties = new TestGlobalProperties(mockedGlobalRepository, mockedSchedulingRepository);
        GlobalHubConfigActions configActions = new GlobalHubConfigActions(mockedGlobalRepository, globalProperties, new ObjectTransformer());

        configActions = Mockito.spy(configActions);
        Mockito.doAnswer(new Answer<RestConnection>() {
            @Override
            public RestConnection answer(final InvocationOnMock invocation) throws Throwable {
                return mockedRestConnection;
            }
        }).when(configActions).createRestConnection(Mockito.any(HubServerConfigBuilder.class));

        Mockito.doNothing().when(configActions).validateHubConfiguration(Mockito.any(HubServerConfigBuilder.class));

        configActions.testConfig(mockUtils.createGlobalRestModel());
        Mockito.verify(mockedRestConnection, Mockito.times(1)).connect();
        Mockito.reset(mockedRestConnection);

        final GlobalHubConfigRestModel fullRestModel = mockUtils.createGlobalRestModel();
        configActions.testConfig(fullRestModel);
        Mockito.verify(mockedRestConnection, Mockito.times(1)).connect();
        Mockito.reset(mockedRestConnection);

        final GlobalHubConfigRestModel restModel = mockUtils.createGlobalRestModel();
        final GlobalHubConfigRestModel partialRestModel = configActions.maskRestModel(restModel);

        Mockito.doAnswer(new Answer<GlobalHubConfigEntity>() {
            @Override
            public GlobalHubConfigEntity answer(final InvocationOnMock invocation) throws Throwable {
                return mockUtils.createGlobalEntity();
            }
        }).when(mockedGlobalRepository).findOne(Mockito.anyLong());

        configActions.testConfig(partialRestModel);
        // TODO verify that the correct model gets passed into channelTestConfig()
        Mockito.verify(mockedRestConnection, Mockito.times(1)).connect();
    }

    @Test
    public void testChannelTestConfig() throws Exception {
        final GlobalHubMockUtils mockUtils = new GlobalHubMockUtils();
        final RestConnection mockedRestConnection = Mockito.mock(RestConnection.class);
        final GlobalHubRepositoryWrapper mockedGlobalRepository = Mockito.mock(GlobalHubRepositoryWrapper.class);
        final GlobalSchedulingRepositoryWrapper mockedSchedulingRepository = Mockito.mock(GlobalSchedulingRepositoryWrapper.class);
        final TestGlobalProperties globalProperties = new TestGlobalProperties(mockedGlobalRepository, mockedSchedulingRepository);
        GlobalHubConfigActions configActions = new GlobalHubConfigActions(mockedGlobalRepository, globalProperties, new ObjectTransformer());
        configActions = Mockito.spy(configActions);
        Mockito.doAnswer(new Answer<RestConnection>() {
            @Override
            public RestConnection answer(final InvocationOnMock invocation) throws Throwable {
                return mockedRestConnection;
            }
        }).when(configActions).createRestConnection(Mockito.any(HubServerConfigBuilder.class));

        Mockito.doNothing().when(configActions).validateHubConfiguration(Mockito.any(HubServerConfigBuilder.class));

        configActions.testConfig(mockUtils.createGlobalRestModel());
        Mockito.verify(mockedRestConnection, Mockito.times(1)).connect();
        Mockito.reset(mockedRestConnection);

        final GlobalHubConfigRestModel restModel = mockUtils.createGlobalRestModel();
        configActions.channelTestConfig(restModel);
        Mockito.verify(mockedRestConnection, Mockito.times(1)).connect();
    }

    @Test
    public void testValidateHubConfiguration() throws Exception {
        final GlobalHubConfigActions configActions = new GlobalHubConfigActions(null, null, null);

        final String url = "https://www.google.com/";
        final String user = "User";
        final String password = "Password";
        HubServerConfigBuilder serverConfigBuilder = new HubServerConfigBuilder();
        serverConfigBuilder.setHubUrl(url);
        serverConfigBuilder.setUsername(user);
        serverConfigBuilder.setUsername(password);

        try {
            configActions.validateHubConfiguration(serverConfigBuilder);
            fail();
        } catch (final AlertFieldException e) {
            assertNotNull(e);
            assertEquals("There were issues with the configuration.", e.getMessage());
            assertTrue(!e.getFieldErrors().isEmpty());
        }

        final HubServerConfigValidator validator = Mockito.mock(HubServerConfigValidator.class);
        serverConfigBuilder = Mockito.spy(serverConfigBuilder);
        Mockito.when(serverConfigBuilder.createValidator()).thenReturn(validator);
        Mockito.when(validator.assertValid()).thenReturn(new ValidationResults());
        try {
            configActions.validateHubConfiguration(serverConfigBuilder);
        } catch (final AlertFieldException e) {
            fail();
        }
    }

    @Test
    public void testCreateRestConnection() throws Exception {
        final GlobalHubConfigActions configActions = new GlobalHubConfigActions(null, null, null);

        final String url = "https://www.google.com/";
        final String apiKey = "User";
        HubServerConfigBuilder serverConfigBuilder = new HubServerConfigBuilder();
        serverConfigBuilder.setHubUrl(url);
        serverConfigBuilder.setApiKey(apiKey);

        // we create this spy to skip the server validation that happens in the build method
        serverConfigBuilder = Mockito.spy(serverConfigBuilder);
        Mockito.doAnswer(new Answer<HubServerConfig>() {
            @Override
            public HubServerConfig answer(final InvocationOnMock invocation) throws Throwable {
                final HubServerConfig hubServerConfig = new HubServerConfig(new URL(url), 0, apiKey, new ProxyInfo(null, 0, null, null), false);
                return hubServerConfig;
            }
        }).when(serverConfigBuilder).build();

        final RestConnection restConnection = configActions.createRestConnection(serverConfigBuilder);
        assertNotNull(restConnection);
    }

    @Override
    public void testConfigurationChangeTriggers() {

    }

}