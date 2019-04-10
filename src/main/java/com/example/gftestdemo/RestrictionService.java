package com.example.gftestdemo;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class RestrictionService {

    @Autowired
    @Qualifier("restrictionsRegion")
    Region restrictionsRegion;

    public  boolean checkRestriction(String key){
       return (boolean) restrictionsRegion.get(key);
    }



}
