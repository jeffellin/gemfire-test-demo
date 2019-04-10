package com.example.gftestdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.geode.cache.*;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.Interest;
import org.springframework.data.gemfire.client.RegexInterest;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;

@Configuration
public class GemfireConfig {

    @Bean
    public ClientRegionFactoryBean<String, Boolean> restrictionsRegion(GemFireCache cache) {
        ClientRegionFactoryBean<String, Boolean> region = new ClientRegionFactoryBean<>();

        Interest interest = new RegexInterest(".*", InterestResultPolicy.KEYS);

        CacheListener[] listeners = {new RestrictionsCacheListener()};

        region.setCache(cache);
        Interest interests[] = {interest};
        region.setInterests(interests);
        region.setCacheListeners(listeners);

        region.setShortcut(ClientRegionShortcut.CACHING_PROXY);

        return region;
    }

    @Bean
    public GemfireTemplate template(Region restrictionsRegion) {
        return new GemfireTemplate(restrictionsRegion);
    }

    @Bean
    public ClientCacheConfigurer clientCacheConfigurer(){
        return  new ClientCacheConfigurer() {
            @Override
            public void configure(String s, ClientCacheFactoryBean clientCacheFactoryBean) {

                clientCacheFactoryBean.setSubscriptionEnabled(true);
            }
        };
    }



}


