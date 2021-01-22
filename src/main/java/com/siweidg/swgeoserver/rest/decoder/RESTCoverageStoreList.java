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

package com.siweidg.swgeoserver.rest.decoder;

import com.siweidg.swgeoserver.rest.decoder.utils.JDOMBuilder;
import com.siweidg.swgeoserver.rest.decoder.utils.NameLinkElem;

import org.jdom.Element;

/**
 * Parses list of summary data about CoverageStores.
 *
 * <P>This is the XML REST representation:
 * <PRE>{@code <coverageStores>
  <coverageStore>
    <name>sfdem</name>
    <atom:link xmlns:atom="http://www.w3.org/2005/Atom"
            rel="alternate"
            href="http://localhost:8080/geoserver/rest/workspaces/sf/coveragestores/sfdem.xml"
            type="application/xml"/>
  </coverageStore>
</coverageStores>
 *
}</PRE>
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class RESTCoverageStoreList extends RESTAbstractList<NameLinkElem> {

    public static RESTCoverageStoreList build(String response) {
        Element elem = JDOMBuilder.buildElement(response);
        return elem == null? null : new RESTCoverageStoreList(elem);
	}

    protected RESTCoverageStoreList(Element list) {
        super(list);
    }
    
}