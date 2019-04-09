package com.example.gftestdemo;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.distributed.ServerLauncher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.tests.integration.ClientServerIntegrationTestsSupport;
import org.springframework.data.gemfire.tests.integration.ForkingClientServerIntegrationTestsSupport;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GemfireTestDemoApplicationTests extends ForkingClientServerIntegrationTestsSupport {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GemfireTemplate template;

    @BeforeClass
    public static void startGemFireServer() throws IOException {
        startGemFireServer(GemFireServerConfiguration.class);
    }

    @AfterClass
    public static void shutdown() throws IOException {
        stopGemFireServer();
    }

    public static class GemFireServerConfiguration {
        public static void main(String[] args) throws InterruptedException {
            int port = Integer.parseInt(System.getProperty(ClientServerIntegrationTestsSupport.GEMFIRE_CACHE_SERVER_PORT_PROPERTY, "40404"));

            ServerLauncher serverLauncher = new ServerLauncher.Builder()
                    .setServerPort(port)
                    .setMemberName("testServer")
                    // Stupid trick to force testServer.log to appear in the current directory
                    .setWorkingDirectory(Paths.get("..").toAbsolutePath().toString())
                    .build();

            serverLauncher.start();

            serverLauncher.waitOnServer();

            Cache cache = CacheFactory.getAnyInstance();
            cache.createRegionFactory(RegionShortcut.REPLICATE).create("restrictionsRegion").put("key", Boolean.TRUE);

            System.out.println(serverLauncher.status());
            Thread.sleep(20000);
        }

    }

    @Test
    public void shouldGetValue() throws InterruptedException {
        log.info("About to access the value by key");
        Thread.sleep(5000); // while we wait the cache connection to server still exists
        assertThat((Boolean) template.get("key")).isTrue();
    }
}
