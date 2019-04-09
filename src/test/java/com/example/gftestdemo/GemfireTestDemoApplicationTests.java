package com.example.gftestdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.data.gemfire.cache.log-level=warn")
public class GemfireTestDemoApplicationTests {

    @Test
    public void loadsContext() {
    }

}
