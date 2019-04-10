package com.example.gftestdemo;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.RegionEvent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableLogging;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.tests.integration.config.ClientServerIntegrationTestsConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.data.gemfire.cache.log-level=warn",
        classes = GemFireTests.GemFireClientConfiguration.class)
public class GemFireTests extends ForkingClientServerIntegrationTestsSupport {


    private static final String GEMFIRE_LOG_LEVEL = "error";

    @Autowired
    private GemfireTemplate template;


    @Autowired
    private RestrictionService restrictionService;

    @BeforeClass
    public static void startGemFireServer() throws IOException {
        startGemFireServer(GemFireServerConfiguration.class);
    }

    @Before()
    public void before() {
        template.put("key", true);
        template.put("restricted", true);
        template.put("notrestricted", false);


    }

    @Test
    public void checkRestricted(){

        assertTrue((restrictionService.checkRestriction("restricted")));
    }

    @Test
    public void checkNotRestricted(){

        assertFalse((restrictionService.checkRestriction("notrestricted")));
    }


    @SpringBootApplication
    @EnableLogging(logLevel = GEMFIRE_LOG_LEVEL)
    @Import({ClientServerIntegrationTestsConfiguration.class, GemfireConfig.class})
    public static class GemFireClientConfiguration {


    }


    @CacheServerApplication(name = "AutoConfiguredContinuousQueryIntegrationTests", logLevel = GEMFIRE_LOG_LEVEL)
    @EnablePdx
    @EnableLocator
    public static class GemFireServerConfiguration {
        public static void main(String[] args) {
            AnnotationConfigApplicationContext applicationContext =
                    new AnnotationConfigApplicationContext(GemFireServerConfiguration.class);

            applicationContext.registerShutdownHook();


        }

        @Bean("restrictionsRegion")
        public PartitionedRegionFactoryBean<String, Boolean> restrictionsRegion(GemFireCache gemfireCache) {


            PartitionedRegionFactoryBean<String, Boolean> restrictionsRegion =
                    new PartitionedRegionFactoryBean<>();

            restrictionsRegion.setCache(gemfireCache);
            restrictionsRegion.setClose(false);
            restrictionsRegion.setPersistent(false);
            return restrictionsRegion;
        }

    }
    @Test
    public void shouldGetValue() throws InterruptedException {


        assertThat((Boolean) template.get("key")).isTrue();

    }

    @Test
    public void shouldNotGetValue() throws InterruptedException {
        assertThat((Boolean) template.get("notKey")).isNull();
    }

}
