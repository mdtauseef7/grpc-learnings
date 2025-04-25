package org.example.sec11;

import org.example.common.AbstractChannelTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec05EagerChannelDemoTest extends AbstractChannelTest {

    private static final Logger log = LoggerFactory.getLogger(Lec05EagerChannelDemoTest.class);

    @Test
    public void eagerChannelDemo(){
        log.info("{}",channel.getState(true));
    }
}
