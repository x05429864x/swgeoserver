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

package it.geosolutions.swgeoserver.rest.publisher;

import it.geosolutions.swgeoserver.rest.GeoServerRESTPublisher.UploadMethod;
import it.geosolutions.swgeoserver.rest.GeoserverRESTTest;
import it.geosolutions.swgeoserver.rest.decoder.*;
import it.geosolutions.swgeoserver.rest.encoder.GSResourceEncoder.ProjectionPolicy;
import it.geosolutions.swgeoserver.rest.encoder.coverage.GSCoverageEncoderTest;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

/**
 * Testcase for publishing layers on geoserver.
 * We need a running GeoServer to properly run the tests. 
 * If such geoserver instance cannot be contacted, tests will be skipped.
 *
 * @author etj
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 */
public class GeoserverRESTShapeTest extends GeoserverRESTTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeoserverRESTShapeTest.class);


    @After
    public void cleanUp(){
    }

    @Test
    public void testList() throws Exception{
        //http://192.168.8.254:8080/geoserver/ceshi2/wms?service=WMS&version=1.1.0&request=GetMap&layers=ceshi2:cities&bbox=-21.8522,28.1388,58.5526,68.9713&width=768&height=390&srs=EPSG:4230&format=application/openlayers3
        //http://192.168.8.254:8080/geoserver/ceshi2/wms?service=WMS&version=1.1.0&request=GetMap&layers=ceshi2:cities&bbox=-21.8522,58.5526,28.1388,68.9713&width=768&height=390&srs=EPSG:4230&format=application/openlayers3
        String url = "http://192.168.8.254:8080/geoserver/";
        String workspace = "ceshi2";
        String layerName = "cities";
        RESTLayer layer1 = reader.getLayer("ceshi2", "cities");
        RESTFeatureType featureType = reader.getFeatureType(layer1);
        RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
        String minX = nativeBoundingBox.getMinX()+",";
        String maxX = nativeBoundingBox.getMaxX()+",";
        String minY = nativeBoundingBox.getMinY()+",";
        String maxY = nativeBoundingBox.getMaxY()+"";


        url+=workspace+"/wms?service=WMS&version=1.1.0&request=GetMap&layers="+workspace+":"+layerName+"&bbox="+minX+minY+maxX+maxY+"&width=768&height=390&srs=EPSG:4230&format=application/openlayers3";

        System.out.println(url);
        featureType.getMaxX();
//        dataStoreList.getNames();
//        double maxX = wms.getMaxX();
//        double maxY = wms.getMaxY();
//
//
//        double minX = wms.getMinX();
//        double minY = wms.getMinY();
//        System.out.println(maxX);
//        System.out.println(maxY);
//        System.out.println(minX);
//        System.out.println(minY);

//        RESTLayer.Type type = layer1.getType();
//
//        System.out.println(layer1.getResourceUrl());
////        XmlUtil.getMapUrl("sf","sf");
//        RESTDataStoreList sd = reader.getDatastores("sf");
//        System.out.println(sd);
//        RESTLayer layer = reader.getLayer("sf", "bugsites");
//        System.out.println(layer);
//        RESTLayerList layers = reader.getLayers();
//        System.out.println(layers);
    }

    @Test
    public void testReloadDataStore() throws FileNotFoundException, IOException {
        if (!enabled())
            return;
        //deleteAllWorkspacesRecursively();
        //存在相应的工作区
        if(!reader.existsWorkspace(DEFAULT_WS)){
        	assertTrue(publisher.createWorkspace(DEFAULT_WS));
        }

        String storeName = "aaaaaaa";
        String layerName = "tokyo";

//        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
        
        File zipFile = new File("C:\\Users\\x\\Desktop\\aoi\\tokyo.zip");
        String nativeCrs = "EPSG:4326";
        String defaultStyle = null;
//        File zipFile = new ClassPathResource("testdata/shapefile/cities.shp").getFile();
        
        // test insert
//        boolean published = publisher.publishShp(DEFAULT_WS, storeName, new NameValuePair[]{new NameValuePair("charset", "UTF-8")},datasetName, UploadMethod.EXTERNAL, zipFile.toURI(), "EPSG:4326",GSCoverageEncoderTest.WGS84,ProjectionPolicy.REPROJECT_TO_DECLARED,"polygon");
        // test insert
//        boolean published = publisher.publishShp(DEFAULT_WS, storeName, new NameValuePair[]{new NameValuePair("charset", "UTF-8")},datasetName, UploadMethod.FILE, zipFile.toURI(), "EPSG:4326",GSCoverageEncoderTest.WGS84,ProjectionPolicy.REPROJECT_TO_DECLARED,"polygon");
//        boolean published = publisher.publishShpCollection(DEFAULT_WS, storeName , zipFile.toURI());        
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile);
               
//        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile, "EPSG:4326",new NameValuePair[]{new NameValuePair("charset", "UTF-8 BOM")});
        

        // test reload
//        assertTrue(publisher.reloadStore(DEFAULT_WS, storeName, StoreType.DATASTORES));
    }

    @Test
    public void testPublishDeleteShapeZip() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
//        //deleteAllWorkspacesRecursively();
        
//        assertTrue(publisher.createWorkspace(DEFAULT_WS));

        String storeName = "resttestshp";
        String layerName = "cities";

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
        
        
        // test insert
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile);
        assertTrue("publish() failed", published);
        assertTrue(existsLayer(layerName));
        // Test exists datastore
        assertTrue(reader.existsDatastore(DEFAULT_WS, storeName));
        // Test exists featuretype
        assertTrue(reader.existsFeatureType(DEFAULT_WS, storeName, layerName));

        RESTLayer layer = reader.getLayer(layerName);

        LOGGER.info("Layer style is " + layer.getDefaultStyle());

        //test delete
        boolean ok = publisher.unpublishFeatureType(DEFAULT_WS, storeName, layerName);
        assertTrue("Unpublish() failed", ok);
        assertFalse(existsLayer(layerName));
        
        // Test not exists featuretype
        assertFalse(reader.existsFeatureType(DEFAULT_WS, storeName, layerName));

        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storeName,false);
//        assertTrue("removeDatastore() failed", dsRemoved);
//        
//        // Test not exists datastore
//        assertFalse(reader.existsDatastore(DEFAULT_WS, storeName));

    }


    @Test
    public void testPublishDeleteExternalComplexShapeZip() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
//        //deleteAllWorkspacesRecursively();
//        Assume.assumeTrue(enabled);
//        assertTrue(publisher.createWorkspace(DEFAULT_WS));

        String storeName = "resttestshp_complex";
        String datasetName = "cities";

        File zipFile = new ClassPathResource("testdata/shapefile/cities.shp").getFile();
        
        // test insert
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, new NameValuePair[]{new NameValuePair("charset", "UTF-8")},datasetName, UploadMethod.EXTERNAL, zipFile.toURI(), "EPSG:4326",GSCoverageEncoderTest.WGS84,ProjectionPolicy.REPROJECT_TO_DECLARED,"polygon");
//        assertTrue("publish() failed", published);
//        assertTrue(existsLayer(datasetName));
        

//        RESTLayer layer = reader.getLayer(datasetName);

//        LOGGER.info("Layer style is " + layer.getDefaultStyle());

//        //test delete
//        boolean ok = publisher.unpublishFeatureType(DEFAULT_WS, storeName, datasetName);
//        assertTrue("Unpublish() failed", ok);
//        assertFalse(existsLayer(datasetName));
//
//        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storeName,false);
//        assertTrue("removeDatastore() failed", dsRemoved);
    }
    
    @Test
    public void testPublishDeleteComplexShapeZip() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
//        //deleteAllWorkspacesRecursively();
//        Assume.assumeTrue(enabled);
//        assertTrue(publisher.createWorkspace(DEFAULT_WS));

        String storeName = "resttestshp_complex";
        String datasetName = "cities";

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
        
        // test insert
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, new NameValuePair[]{new NameValuePair("charset", "UTF-8")},datasetName, UploadMethod.FILE, zipFile.toURI(), "EPSG:4326",GSCoverageEncoderTest.WGS84,ProjectionPolicy.REPROJECT_TO_DECLARED,"polygon");
//        assertTrue("publish() failed", published);
//        assertTrue(existsLayer(datasetName));


//        RESTLayer layer = reader.getLayer(datasetName);
//
//        LOGGER.info("Layer style is " + layer.getDefaultStyle());

//        //test delete
//        boolean ok = publisher.unpublishFeatureType(DEFAULT_WS, storeName, datasetName);
//        assertTrue("Unpublish() failed", ok);
//        assertFalse(existsLayer(datasetName));
//
//        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storeName,false);
//        assertTrue("removeDatastore() failed", dsRemoved);
    }

    @Test
    public void testPublishDeleteStyledShapeZip() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
        //deleteAllWorkspacesRecursively();
//        Assume.assumeTrue(enabled);
        if(!reader.getWorkspaceNames().contains(DEFAULT_WS)) {
        	assertTrue(publisher.createWorkspace(DEFAULT_WS));
        }else {
        	System.out.println("工作区："+DEFAULT_WS+",已存在！");
        }

//        String ns = "solutions";
        String storeName = "resttestshp";
        String layerName = "cities";
        final String styleName = "restteststyle";

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
//        publisher.removeDatastore(DEFAULT_WS, storeName,true);
//        publisher.removeStyle(styleName);
        
        File sldFile = new ClassPathResource("testdata/restteststyle.sld").getFile();

        // insert style
        if(!reader.existsStyle(styleName)){
        	boolean sldpublished = publisher.publishStyle(sldFile); // Will take the name from sld contents
        	assertTrue("style publish() failed", sldpublished);
        }else {
        	System.out.println("styleName："+styleName+"已存在！");
        }

        // test insert
        if(!existsLayer(layerName)) {
        	boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile, "EPSG:4326", styleName);
            System.out.println(published);
            assertTrue("publish() failed", published);
        }else {
        	System.out.println("layerName："+layerName+"已存在！");
        }
        

        RESTLayer layer = reader.getLayer(layerName);
//        RESTLayer layerDecoder = new RESTLayer(layer);
        LOGGER.info("Layer style is " + layer.getDefaultStyle());
        assertEquals("Style not assigned properly", styleName, layer.getDefaultStyle());

//        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(ns, storeName,true);
//        assertTrue("removeDatastore() failed", dsRemoved);
//
//        //test delete style
//        boolean oksld = publisher.removeStyle(styleName);
//        assertTrue("Unpublish() failed", oksld);
//        assertFalse(reader.existsStyle(styleName));
    }
    
    @Test
    public void testPublishDeleteStyledInWorkspaceShapeZip() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
        //deleteAllWorkspacesRecursively();
//        Assume.assumeTrue(enabled);
//        assertTrue(publisher.createWorkspace(DEFAULT_WS));

//        String ns = "geosolutions";
        String storeName = "resttestshp";
        String layerName = "cities";
        final String styleName = "restteststyle";

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
//        publisher.removeDatastore(DEFAULT_WS, storeName,true);
//        publisher.removeStyleInWorkspace(DEFAULT_WS, styleName);
        
        File sldFile = new ClassPathResource("testdata/restteststyle.sld").getFile();

        // insert style
        boolean sldpublished = publisher.publishStyleInWorkspace(DEFAULT_WS, sldFile); // Will take the name from sld contents
        assertTrue("style publish() failed", sldpublished);
        assertTrue(reader.existsStyle(DEFAULT_WS, styleName));

        // test insert
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile, "EPSG:4326", DEFAULT_WS + ":" + styleName);
        assertTrue("publish() failed", published);
        assertTrue(existsLayer(layerName));

        RESTLayer layer = reader.getLayer(layerName);
//        RESTLayer layerDecoder = new RESTLayer(layer);
        LOGGER.info("Layer style is " + layer.getDefaultStyle());
        assertEquals("Style not assigned properly", DEFAULT_WS + ":" + styleName, layer.getDefaultStyle());
        assertEquals("Style not assigned properly", DEFAULT_WS, layer.getDefaultStyleWorkspace());

//        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storeName,true);
//        assertTrue("removeDatastore() failed", dsRemoved);
//
//        //test delete style
//        boolean oksld = publisher.removeStyleInWorkspace(DEFAULT_WS, styleName);
//        assertTrue("Unpublish() failed", oksld);
//        assertFalse(reader.existsStyle(styleName));
    }

    @Test
    public void testPublishDeleteShapeZipWithParams() throws FileNotFoundException, IOException {
        if (!enabled()) {
            return;
        }
        //deleteAllWorkspacesRecursively();
//        Assume.assumeTrue(enabled);
        
        String workspace = DEFAULT_WS;
        String storeName = "resttestshp";
        String layerName = "cities";

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();

        // known state?
//        publisher.removeDatastore(DEFAULT_WS, storeName,true);
        if(!reader.getWorkspaceNames().contains(workspace)) {
        	assertTrue(publisher.createWorkspace(workspace));
        }else {
        	System.out.println("工作区："+workspace+",已存在！");
        }

        if(!reader.getDatastores(workspace).getNames().contains(storeName)) {
        	 // test insert
        	boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile,"EPSG:4326",new NameValuePair("charset","UTF-8"));
//            assertTrue("publish() failed", published);
//            assertTrue(existsLayer(layerName));
        }else {
        	System.out.println("数据存储："+storeName+",已存在！");
        }
       
        

//        RESTLayer layer = reader.getLayer(layerName);

//        LOGGER.info("Layer style is " + layer.getDefaultStyle());

        //test delete
        boolean ok = publisher.unpublishFeatureType(DEFAULT_WS, storeName, layerName);
        assertTrue("Unpublish() failed", ok);
//        assertFalse(existsLayer(layerName));

        /*// remove also datastore
        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storeName);
        assertTrue("removeDatastore() failed", dsRemoved);*/

    }

    /**
     * Test case to solve error described in:
     * https://github.com/geosolutions-it/geoserver-manager/issues/11
     * 
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    @Test
    public void testPublishShpUsingDeclaredNativeCRS() throws Exception {
        //deleteAllWorkspacesRecursively();
        
        // layer publication params
        String workspace = DEFAULT_WS;
        String storename = "resttestshp";
        String layerName = "cities";
        File zipFile = new ClassPathResource("testdata/testshp_no_prj.zip")
                .getFile();
        String nativeCrs = "EPSG:4230";
        String defaultStyle = null;

        // Cleanup
        //deleteAllWorkspacesRecursively();
        if(!reader.getWorkspaceNames().contains(workspace)) {
        	assertTrue(publisher.createWorkspace(workspace));
        }else {
        	System.out.println("工作区："+workspace+",已存在！");
        }

        if(!reader.getDatastores(workspace).getNames().contains(storename)) {
        	// Publish layer
            assertTrue(publisher.publishShp(workspace, storename, layerName,
                    zipFile, nativeCrs, defaultStyle));

            // Read CRS. Should be using the one indicated at publication time.
//            assertNotNull(reader.getLayer(layerName));
        }else {
        	System.out.println("数据存储："+storename+",已存在！");
        }
        
    }
        	
       

        
        
        // remove also datastore
/*        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storename,true);
        assertTrue("removeDatastore() failed", dsRemoved);
    }
    
    /**
     * Test case to solve error described in:
     * https://github.com/geosolutions-it/geoserver-manager/issues/11
     * 
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    @Test
    public void testPublishShpUsingWKTNativeCRS() throws Exception {
        if (!enabled())
            return;
        //deleteAllWorkspacesRecursively();
        
        // layer publication params
        String workspace = DEFAULT_WS;
        String storename = "resttestshp";
        String layerName = "10m_populated_places";
        File zipFile = new ClassPathResource("testdata/test_noepsg.zip")
                .getFile();
        String nativeCrs = "EPSG:4326";
        String defaultStyle = null;

        // Cleanup
        //deleteAllWorkspacesRecursively();
        assertTrue(publisher.createWorkspace(workspace));

        // Publish layer
//        assertTrue(publisher.publishShp(workspace, storename, layerName,
//                zipFile, nativeCrs, defaultStyle));
        publisher.publishShp(workspace, storename, layerName,
                zipFile, nativeCrs, defaultStyle);
        // Read CRS. Should be using the one indicated at publication time.
//        assertNotNull(reader.getLayer(layerName));
        
        // remove also datastore
//        boolean dsRemoved = publisher.removeDatastore(DEFAULT_WS, storename,true);
//        assertTrue("removeDatastore() failed", dsRemoved);
    }

    
}
