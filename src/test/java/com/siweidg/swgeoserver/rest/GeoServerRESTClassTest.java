/*
 *  GeoServer-Manager - Simple Manager Library for GeoServer
 *  
 *  Copyright (C) 2007,2011 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.siweidg.swgeoserver.rest;

import com.siweidg.swgeoserver.rest.decoder.RESTLayerGroup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Simple class for testing that the DeleteAllLayerGroups() method behaves correctly.
 * 
 * @author Nicola Lagomarsini 
 */
public class GeoServerRESTClassTest extends GeoserverRESTTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoserverRESTTest.class);;

    @Test
    public void testGetLayerGroups() {
        if(!enabled()){
            return;
        }
        List<String> groups = reader.getLayerGroups().getNames();
        LOGGER.info("Found " + groups.size() + " layerGroups");
        for (String groupName : groups) {
            RESTLayerGroup group = reader.getLayerGroup(groupName);
            if (groups != null) {
                assertNotNull(group.getPublishedList());
            }
        }
    }
}
