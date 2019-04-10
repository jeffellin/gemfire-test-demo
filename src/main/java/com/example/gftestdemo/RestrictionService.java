package com.example.gftestdemo;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class RestrictionService {

    @Resource(name = "restrictionRegion")
    Region<String,Boolean> restrictionRegion;

    public  boolean checkRestriction(String key){
       return restrictionRegion.get(key);
    }



}
