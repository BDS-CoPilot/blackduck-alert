package com.blackducksoftware.integration.hub.alert.digest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.blackducksoftware.integration.hub.alert.OutputLogger;
import com.blackducksoftware.integration.hub.alert.channel.ChannelTemplateManager;
import com.blackducksoftware.integration.hub.alert.event.AbstractChannelEvent;

public class DigestItemWriterTest {
    private OutputLogger outputLogger;

    @Before
    public void init() throws IOException {
        outputLogger = new OutputLogger();
    }

    @Test
    public void testWrite() throws Exception {
        final ChannelTemplateManager channelTemplateManager = Mockito.mock(ChannelTemplateManager.class);
        Mockito.doNothing().when(channelTemplateManager).sendEvents(Mockito.anyListOf(AbstractChannelEvent.class));

        final DigestItemWriter digestItemWriter = new DigestItemWriter(channelTemplateManager);

        final AbstractChannelEvent abstractChannelEvent = Mockito.mock(AbstractChannelEvent.class);
        final List<AbstractChannelEvent> channelList = Arrays.asList(abstractChannelEvent);
        digestItemWriter.write(Arrays.asList(channelList));

        Mockito.verify(channelTemplateManager).sendEvents(Mockito.anyListOf(AbstractChannelEvent.class));
    }

    @Test
    public void testWriteNull() throws Exception {
        final ChannelTemplateManager channelTemplateManager = Mockito.mock(ChannelTemplateManager.class);
        Mockito.doThrow(new NullPointerException()).when(channelTemplateManager).sendEvents(Mockito.anyListOf(AbstractChannelEvent.class));

        final DigestItemWriter digestItemWriter = new DigestItemWriter(channelTemplateManager);

        final AbstractChannelEvent abstractChannelEvent = Mockito.mock(AbstractChannelEvent.class);
        final List<AbstractChannelEvent> channelList = Arrays.asList(abstractChannelEvent);
        digestItemWriter.write(Arrays.asList(channelList));

        final boolean exceptionThrown = outputLogger.isLineContainingText("Error occurred writing digest notification data to channels");

        assertTrue(exceptionThrown);
    }
}
