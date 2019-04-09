package com.example.gftestdemo;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.distributed.ServerLauncher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.tests.integration.ClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.data.gemfire.cache.log-level=warn")
public class GemFireTests extends ForkingClientServerIntegrationTestsSupport {

    @Autowired
    private GemfireTemplate template;

    @BeforeClass
    public static void startGemFireServer() throws IOException {
        startGemFireServer(GemFireServerConfiguration.class);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        closeGemFireCacheWaitOnCloseEvent();
        stopGemFireServer();
    }

    public static class GemFireServerConfiguration {
        public static void main(String[] args) throws InterruptedException {
            int port = Integer.parseInt(System.getProperty(ClientServerIntegrationTestsSupport.GEMFIRE_CACHE_SERVER_PORT_PROPERTY, "40404"));

            ServerLauncher serverLauncher = new ServerLauncher.Builder()
                    .setServerPort(port)
                    .setMemberName("testServer")
                    .set("log-level", "info")
                    .build();

            serverLauncher.start();

            serverLauncher.waitOnServer();

            Cache cache = CacheFactory.getAnyInstance();
            cache.createRegionFactory(RegionShortcut.REPLICATE).create("restrictionsRegion").put("key", Boolean.TRUE);

            // Wait forever until the process is killed
            CountDownLatch infiniteLatch = new CountDownLatch(1);
            infiniteLatch.await();
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
