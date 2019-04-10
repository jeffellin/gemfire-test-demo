package com.example.gftestdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

@Slf4j
public class RestrictionCacheListener<String, Boolean> implements CacheListener<String, Boolean> {

    @Override
    public void afterCreate(EntryEvent<String, Boolean> entryEvent) {
        log.error("\n\tafterCreate received for key: "+entryEvent.getNewValue());
    }

    @Override
    public void afterUpdate(EntryEvent<String, Boolean> entryEvent) {

    }

    @Override
    public void afterInvalidate(EntryEvent<String, Boolean> entryEvent) {

    }

    @Override
    public void afterDestroy(EntryEvent<String, Boolean> entryEvent) {

    }

    @Override
    public void afterRegionInvalidate(RegionEvent<String, Boolean> regionEvent) {

    }

    @Override
    public void afterRegionDestroy(RegionEvent<String, Boolean> regionEvent) {

    }

    @Override
    public void afterRegionClear(RegionEvent<String, Boolean> regionEvent) {

    }

    @Override
    public void afterRegionCreate(RegionEvent<String, Boolean> regionEvent) {

    }

    @Override
    public void afterRegionLive(RegionEvent<String, Boolean> regionEvent) {

    }

    @Override
    public void close() {

    }
}