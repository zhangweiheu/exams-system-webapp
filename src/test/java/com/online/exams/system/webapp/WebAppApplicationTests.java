package com.online.exams.system.webapp;

import com.online.exams.system.webapp.config.WebappConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by 36kr on 16/1/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebappConfiguration.class)
@WebAppConfiguration
public class WebAppApplicationTests {
    @Test
    public void contextLoads() {
    }
}
