package com.example.gftestdemo;

import org.apache.geode.cache.Region;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GemfireMockTests {

    @Mock
    private Region restrictionRegion;

    @InjectMocks
    private RestrictionService restrictionService;

    @Test
    public void checkRestricted(){
        when(restrictionRegion.get("restricted")).thenReturn(true);

        assertTrue((restrictionService.checkRestriction("restricted")));
    }

    @Test
    public void checkNotRestricted(){
        when(restrictionRegion.get("notrestricted")).thenReturn(false);

        assertFalse((restrictionService.checkRestriction("notrestricted")));
    }

}
