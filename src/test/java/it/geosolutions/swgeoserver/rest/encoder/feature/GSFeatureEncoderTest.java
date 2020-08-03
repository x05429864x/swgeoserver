/*
 *  Copyright (C) 2007 - 2015 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 * 
 *  GPLv3 + Classpath exception
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.swgeoserver.rest.encoder.feature;

import it.geosolutions.swgeoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.swgeoserver.rest.GeoserverRESTTest;
import it.geosolutions.swgeoserver.rest.decoder.RESTFeatureType;
import it.geosolutions.swgeoserver.rest.decoder.RESTLayer;
import it.geosolutions.swgeoserver.rest.decoder.about.GSVersionDecoder.VERSION;
import it.geosolutions.swgeoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.swgeoserver.rest.encoder.GSLayerEncoder21;
import it.geosolutions.swgeoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.swgeoserver.rest.encoder.authorityurl.GSAuthorityURLInfoEncoder;
import it.geosolutions.swgeoserver.rest.encoder.identifier.GSIdentifierInfoEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadata.GSDimensionInfoEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadata.GSDimensionInfoEncoder.Presentation;
import it.geosolutions.swgeoserver.rest.encoder.metadata.GSFeatureDimensionInfoEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadata.virtualtable.GSVirtualTableEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadata.virtualtable.VTGeometryEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadata.virtualtable.VTParameterEncoder;
import it.geosolutions.swgeoserver.rest.encoder.metadatalink.GSMetadataLinkInfoEncoder;
import it.geosolutions.swgeoserver.rest.encoder.utils.ElementUtils;
import org.jdom.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 
 * Note on adding multiple available styles to the GSLayerEncoder: - to run the testIntegration(), 2 clones of the "point" style, named "point2" and
 * "point3" have to be created.
 * 
 * @author ETj (etj at geo-solutions.it)
 * @author Carlo Cancellieri - carlo.cancellieri@geo-solutions.it
 * @author Emmanuel Blondel - emmanuel.blondel1@gmail.com | emmanuel.blondel@fao.org
 */
public class GSFeatureEncoderTest extends GeoserverRESTTest {
    protected final static Logger LOGGER = LoggerFactory.getLogger(GSFeatureEncoderTest.class);

    @Test
    public void testIntegration() throws IOException {

        if (!enabled())
            return;
//        deleteAll();

        GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
                            
        String storeName = "resttestshp";
        String layerName = "cities";
        
        publisher.createWorkspace(DEFAULT_WS);

        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();

        // test insert
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile);
        assertTrue("publish() failed", published);
        assertTrue(existsLayer(layerName));

        publisher.publishStyle(new File(new ClassPathResource("testdata").getFile(),
                "default_point.sld"));

        GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
        fte.setNativeName(layerName);
        fte.setName(layerName + "_NEW");
        fte.setTitle("title");
        // fte.addKeyword("TODO");
        fte.setNativeCRS("EPSG:4326");
        fte.setDescription("desc");
        fte.setEnabled(true);

        // metadataLink
        GSMetadataLinkInfoEncoder metadatalink = new GSMetadataLinkInfoEncoder("text/xml",
                "ISO19115:2003", "http://www.organization.org/metadata1");
        fte.addMetadataLinkInfo(metadatalink);

        GSLayerEncoder layerEncoder = null;
        if (VERSION.getVersion(GS_VERSION) == VERSION.UNRECOGNIZED ) {
            layerEncoder = new GSLayerEncoder21();
        } else {
            layerEncoder = new GSLayerEncoder();
        }

        layerEncoder.setEnabled(true);
        layerEncoder.setQueryable(true);
        layerEncoder.setAdvertised(true);

        layerEncoder.setDefaultStyle("point");
        layerEncoder.addStyle("point2");
        layerEncoder.addStyle("point3");

        // authorityURL
        GSAuthorityURLInfoEncoder authorityURL = new GSAuthorityURLInfoEncoder("authority1",
                "http://www.authority1.org");
        layerEncoder.addAuthorityURL(authorityURL);

        // identifier
        GSIdentifierInfoEncoder identifier1 = new GSIdentifierInfoEncoder("authority1",
                "identifier1");
        GSIdentifierInfoEncoder identifier2 = new GSIdentifierInfoEncoder("authority1",
                "another_identifier");
        layerEncoder.addIdentifier(identifier1);
        layerEncoder.addIdentifier(identifier2);


        // optionally select the attributes to publish
        RESTLayer layer = reader.getLayer(DEFAULT_WS, layerName);
        RESTFeatureType resource = reader.getFeatureType(layer);
        List<GSAttributeEncoder> attrs = resource.getEncodedAttributeList();
        assertNotNull(attrs);
        for (GSAttributeEncoder enc : attrs) {
            fte.setAttribute(enc);
        }

        assertTrue(publisher.publishDBLayer(DEFAULT_WS, storeName, fte, layerEncoder));

    }

    @Test
    public void testFeatureTypeEncoder() {

        GSFeatureTypeEncoder encoder = new GSFeatureTypeEncoder();
        encoder.addKeyword("KEYWORD_1");
        encoder.addKeyword("KEYWORD_2");
        encoder.addKeyword("...");
        encoder.addKeyword("KEYWORD_N");

        encoder.setName("Layername");

        encoder.setTitle("title");
        encoder.addKeyword("TODO");
        encoder.setNativeCRS("EPSG:4326");
        encoder.setDescription("desc");
        encoder.setEnabled(true);

        GSAttributeEncoder attribute = new GSAttributeEncoder();
        attribute.setAttribute(FeatureTypeAttribute.name, "NAME");
        attribute.setAttribute(FeatureTypeAttribute.binding, "java.lang.String");
        attribute.setAttribute(FeatureTypeAttribute.maxOccurs, "1");
        attribute.setAttribute(FeatureTypeAttribute.minOccurs, "0");
        attribute.setAttribute(FeatureTypeAttribute.nillable, "true");

        encoder.setAttribute(attribute);

        encoder.delAttribute("NAME");

        attribute.setAttribute(FeatureTypeAttribute.name, "NEW_NAME");

        encoder.setAttribute(attribute);

        // TODO encoder.getAttribute("NAME");

        GSFeatureDimensionInfoEncoder dim2 = new GSFeatureDimensionInfoEncoder("ELE");

        encoder.setMetadataDimension("elevation", dim2);
        dim2.setPresentation(Presentation.DISCRETE_INTERVAL, BigDecimal.valueOf(10));
        Element el = ElementUtils.contains(encoder.getRoot(), GSDimensionInfoEncoder.PRESENTATION);
        assertNotNull(el);

        LOGGER.info("contains_key:" + el.toString());

        dim2.setPresentation(Presentation.DISCRETE_INTERVAL, BigDecimal.valueOf(12));
        el = ElementUtils.contains(encoder.getRoot(), GSDimensionInfoEncoder.RESOLUTION);
        assertNotNull(el);
        assertEquals("12", el.getText());

        dim2.setPresentation(Presentation.CONTINUOUS_INTERVAL);

        encoder.setMetadataDimension("time", new GSFeatureDimensionInfoEncoder("time"));
        el = ElementUtils.contains(encoder.getRoot(), GSDimensionInfoEncoder.PRESENTATION);
        assertNotNull(el);
        el = ElementUtils.contains(encoder.getRoot(), GSDimensionInfoEncoder.RESOLUTION);
        assertNull(el);

        el = ElementUtils.contains(encoder.getRoot(), GSResourceEncoder.METADATA);
        assertNotNull(el);
        LOGGER.info("contains_key:" + el.toString());

        final boolean removed = ElementUtils.remove(encoder.getRoot(), el);
        LOGGER.info("remove:" + removed);
        assertTrue(removed);

        el = ElementUtils.contains(encoder.getRoot(), "metadata");
        assertNull(el);
        if (el == null)
            LOGGER.info("REMOVED");

        if (LOGGER.isInfoEnabled())
            LOGGER.info(encoder.toString());

        assertEquals(encoder.getName(), "Layername");
    }

    @Test
    public void testModifyFeature() {
        GSFeatureTypeEncoder encoder = new GSFeatureTypeEncoder();
        encoder.addKeyword("KEYWORD_1");
        encoder.addKeyword("KEYWORD_1", "LAN_1", "VOCAB_1");
        assertTrue(encoder.delKeyword("KEYWORD_1", "LAN_1", "VOCAB_1"));

        encoder.addKeyword("...");
        encoder.addKeyword("KEYWORD_N");
        assertFalse(encoder.delKeyword("KEYWORD_M"));

        encoder.addKeyword("KEYWORD_2");
        assertFalse(encoder.delKeyword("KEYWORD_2", "LAN_1", "VOCAB_1"));
        assertTrue(encoder.delKeyword("KEYWORD_2"));

        // metadataLinkInfo
        encoder.addMetadataLinkInfo("text/xml", "ISO19115:2003",
                "http://www.organization.org/metadata1");
        encoder.addMetadataLinkInfo("text/html", "ISO19115:2003",
                "http://www.organization.org/metadata2");

        assertTrue(encoder.delMetadataLinkInfo("http://www.organization.org/metadata2"));
        assertFalse(encoder.delMetadataLinkInfo("http://www.organization.org/metadata3"));

        // dimensions
        final GSFeatureDimensionInfoEncoder elevationDimension = new GSFeatureDimensionInfoEncoder(
                "elevation_field");

        // if (LOGGER.isInfoEnabled())
        // LOGGER.info(encoder.toString());

        final String metadata = "elevation";
        encoder.setMetadataDimension(metadata, elevationDimension);

        elevationDimension.setPresentation(Presentation.DISCRETE_INTERVAL, BigDecimal.valueOf(10));

        if (LOGGER.isInfoEnabled())
            LOGGER.info(encoder.toString());

        assertTrue(encoder.delMetadata(metadata));

        if (LOGGER.isInfoEnabled())
            LOGGER.info(encoder.toString());

        final Element el = ElementUtils.contains(encoder.getRoot(),
                GSDimensionInfoEncoder.DIMENSIONINFO);
        assertNull(el);
        if (el == null)
            LOGGER.info("REMOVED");

    }

    /**
     * Test method for virtual table encoding / SQL view layer integration
     * 
     * Settings information for integration tests - test is based on the data used in
     * http://docs.geoserver.org/latest/en/user/data/database/sqlview.html#parameterizing-sql-views (states shapefile - available in
     * testdata/states.zip) - create a postgis db - import the states shapefile (using shp2pgsql or Postgis shapefile uploader) - In Geoserver, create
     * a postgis datastore for this DB, with the name "statesdb"
     * 
     * @throws IOException
     * 
     */
    @Test
    public void testSQLViewIntegration() throws IOException {

        if (!enabled())
            return;
//        deleteAll();
        
        GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);

        String storeName = "resttestshp";
        String layerName = "cities";
        
        // build the store
        publisher.createWorkspace(DEFAULT_WS);
        
        // test insert
        File zipFile = new ClassPathResource("testdata/resttestshp.zip").getFile();
        boolean published = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile);
        assertTrue("publish() failed", published);

        publisher.publishStyle(new File(new ClassPathResource("testdata").getFile(),
                "default_point.sld"));

        String nativeName = layerName;
        layerName=layerName+"_NEW";
        
        GSFeatureTypeEncoder fte = new GSFeatureTypeEncoder();
        fte.setName(layerName);
        fte.setNativeName(nativeName);
        fte.setTitle("title");

        fte.addKeyword("keyword1");
        fte.addKeyword("keyword2");
        fte.setNativeCRS("EPSG:4326");
        fte.setDescription("desc");
        fte.setEnabled(true);

        // virtual table
        // -------------
        // Set-up the vtGeom
        final VTGeometryEncoder vtGeom = new VTGeometryEncoder("the_geom", "Point", "4326");

        // Set-up 2 virtual table parameters
        final VTParameterEncoder vtParam1 = new VTParameterEncoder("high", "100000000", "^[\\d]+$");
        final VTParameterEncoder vtParam2 = new VTParameterEncoder("low", "0", "^[\\d]+$");

        // sql
        String sql = "select gid, state_name, the_geom from pgstates where persons between %low% and %high% and state_abbr = '%state%'";

        // set-up the virtual table
        final GSVirtualTableEncoder vte = new GSVirtualTableEncoder();
        vte.setName(nativeName);
        vte.setSql(sql);
        vte.addVirtualTableGeometry(vtGeom);
        vte.addVirtualTableParameter(vtParam1);
        vte.addVirtualTableParameter(vtParam2);

        // modif the vte
        vte.delVirtualTableGeometry("the_geom");
        vte.addVirtualTableGeometry("the_geom", "MultiPolygon", "4326");

        final VTParameterEncoder vtParam3 = new VTParameterEncoder("state", "FL", "^[\\w\\d\\s]+$");
        vte.addVirtualTableParameter(vtParam3);
        vte.addKeyColumn("gid");

        fte.setMetadataVirtualTable(vte); // Set the virtual table

        // Layer encoder
        // -------------
        GSLayerEncoder layerEncoder = new GSLayerEncoder();
        layerEncoder.setEnabled(true);
        layerEncoder.setQueryable(true);
        layerEncoder.setDefaultStyle("polygon");
        
        // test insert
        // ------------
        publisher.createWorkspace(DEFAULT_WS);
        published = publisher.publishDBLayer(DEFAULT_WS, storeName, fte, layerEncoder);
        assertTrue("Publication unsuccessful", published);
        assertTrue("Layer does not exist", existsLayer(layerName));

    }
}
