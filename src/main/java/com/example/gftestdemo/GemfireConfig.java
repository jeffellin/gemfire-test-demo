package com.example.gftestdemo;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;

@Configuration
public class GemfireConfig {

    @Bean
    public ClientRegionFactoryBean<String, Boolean> restrictionsRegion(GemFireCache cache) {
        ClientRegionFactoryBean<String, Boolean> region = new ClientRegionFactoryBean<>();

        region.setCache(cache);
        region.setClose(false);
        region.setShortcut(ClientRegionShortcut.PROXY);

        return region;
    }

    @Bean
    public GemfireTemplate template(Region restrictionsRegion) {
        return new GemfireTemplate(restrictionsRegion);
    }

}
