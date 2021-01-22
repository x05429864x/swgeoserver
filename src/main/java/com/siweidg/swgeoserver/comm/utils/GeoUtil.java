package com.siweidg.swgeoserver.comm.utils;

import com.siweidg.swgeoserver.comm.base.BaseGeoserverREST;
import com.siweidg.swgeoserver.rest.decoder.RESTBoundingBox;
import com.siweidg.swgeoserver.rest.decoder.RESTFeatureType;
import com.siweidg.swgeoserver.rest.decoder.RESTLayer;

public class GeoUtil extends BaseGeoserverREST {



    public static String toUrl(String workspace, String layerName){
        try {
            StringBuffer bbox = new StringBuffer("");
            RESTLayer layer = reader.getLayer(workspace, layerName);
            RESTFeatureType featureType = reader.getFeatureType(layer);
            String crs = featureType.getCRS();
            RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
            bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
//        String minX = nativeBoundingBox.getMinX()+",";
//        String minY = nativeBoundingBox.getMinY()+",";
//        String maxX = nativeBoundingBox.getMaxX()+",";
//        String maxY = nativeBoundingBox.getMaxY()+"";
            StringBuffer sBuffer = new StringBuffer(URL_ADD);
            sBuffer.append(workspace).append("/wms?service=WMS&version=1.1.0&request=GetMap&layers=").append(workspace)
                    .append(":").append(layerName).append("&bbox=").append(bbox).append("&width=768&height=390&srs=")
                    .append(crs).append("&format=application/openlayers3");
//        url+="/"+workspace+"/wms?service=WMS&version=1.1.0&request=GetMap&layers="+workspace+":"+layerName+"&bbox="+minX+minY+maxX+maxY+"&width=768&height=390&srs=EPSG:4230&format=application/openlayers3";
            System.out.println(sBuffer);
            return sBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toGeoParams(String workspace, String layerName){
        try {
            StringBuffer bbox = new StringBuffer("");
            RESTLayer layer = reader.getLayer(workspace, layerName);
            RESTFeatureType featureType = reader.getFeatureType(layer);
            String crs = featureType.getCRS();
            RESTBoundingBox nativeBoundingBox = featureType.getNativeBoundingBox();
            bbox.append(nativeBoundingBox.getMinX()+",").append(nativeBoundingBox.getMinY()+",").append(nativeBoundingBox.getMaxX()+",").append(nativeBoundingBox.getMaxY()+"");
//        String minX = nativeBoundingBox.getMinX()+",";
//        String minY = nativeBoundingBox.getMinY()+",";
//        String maxX = nativeBoundingBox.getMaxX()+",";
//        String maxY = nativeBoundingBox.getMaxY()+"";
            StringBuffer sBuffer = new StringBuffer(URL_ADD);
            sBuffer.append(bbox)
                    .append(","+crs);
//        url+="/"+workspace+"/wms?service=WMS&version=1.1.0&request=GetMap&layers="+workspace+":"+layerName+"&bbox="+minX+minY+maxX+maxY+"&width=768&height=390&srs=EPSG:4230&format=application/openlayers3";
            System.out.println(sBuffer);
            return sBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
