package com.blackducksoftware.integration.hub.alert.channel.email.controller.global;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackducksoftware.integration.hub.alert.channel.email.controller.global.GlobalEmailConfigRestModel;
import com.blackducksoftware.integration.hub.alert.channel.email.mock.MockEmailGlobalEntity;
import com.blackducksoftware.integration.hub.alert.channel.email.mock.MockEmailGlobalRestModel;
import com.blackducksoftware.integration.hub.alert.channel.email.repository.global.GlobalEmailConfigEntity;
import com.blackducksoftware.integration.hub.alert.channel.email.repository.global.GlobalEmailRepository;
import com.blackducksoftware.integration.hub.alert.web.controller.GlobalControllerTest;

public class GlobalEmailConfigControllerTestIT extends GlobalControllerTest<GlobalEmailConfigEntity, GlobalEmailConfigRestModel, GlobalEmailRepository> {

    @Autowired
    GlobalEmailRepository globalEmailRepository;

    @Override
    public GlobalEmailRepository getGlobalEntityRepository() {
        return globalEmailRepository;
    }

    @Override
    public MockEmailGlobalEntity getGlobalEntityMockUtil() {
        return new MockEmailGlobalEntity();
    }

    @Override
    public MockEmailGlobalRestModel getGlobalRestModelMockUtil() {
        return new MockEmailGlobalRestModel();
    }

    @Override
    public String getRestControllerUrl() {
        return "/configuration/global/email";
    }

}