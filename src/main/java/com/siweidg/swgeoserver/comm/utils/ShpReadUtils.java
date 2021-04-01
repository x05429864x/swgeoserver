package com.siweidg.swgeoserver.comm.utils;

import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.*;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.expression.Expression;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;

public class ShpReadUtils {
	public static void main(String[] args) {
		/*long starTime=System.currentTimeMillis();
		System.out.println(starTime);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} 
		long endTime=System.currentTimeMillis();
		System.out.println(endTime);
		long Time=endTime-starTime;
		System.out.println("time:"+Time);*/
		/*ShpReadUtils shp = new ShpReadUtils();
		String filepath = "C:/Users/x/Desktop/tmp1/sss";
		//String suffix = "dbf";
		String code = "UTF-8";
//		shp.write(filepath,"aoi_4_7_17_100");
		List<Map<String, Object>> list = shp.readSHP(filepath + ".shp",code);
		for(Map<String,Object> map :list) {
			for(String str:map.keySet()) {
				System.out.println(str+" = "+map.get(str));
			}
			System.out.println("---------------------------黄金分割-----------------------------");
		}*/
//		shp.readShpGI(filepath + ".shp");
//		shp.readDBF(filepath + ".dbf");
//		shp.readSHP(filepath + ".shp");
//		shp.readDBF2(filepath + ".dbf");
//		shp.readSld(filepath+".shp");
		
		//String path = "D:\\shpFile\\tmpSHP\\test.sld";
		//shp.readSld(path);
		List<Map> map= new ArrayList<Map> ();
		Map m = new HashMap();
		m.put("contract_no", "22");
		m.put("sceneno", "22");
		m.put("order_no", "11");
		m.put("aoi_area", "11");
		//点
//		m.put("the_geom", "POINT (116.45224995263673 39.988118596136665)");
		//点
//		m.put("the_geom", "POINT (116.45504916791398 39.9854922081935)");
		//100km半径圆
//		m.put("the_geom", "POLYGON ((117.45224995263672 39.988073630126955, 117.43303523303994 39.79298330811083, 117.37612948514801 39.605390197761864, 117.28371956493926 39.432503397107354, 117.15935673382327 39.28096684894041, 117.00782018565633 39.15660401782441, 116.8349333850018 39.06419409761567, 116.64734027465285 39.00728834972372, 116.45224995263672 38.988073630126955, 116.25715963062059 39.00728834972372, 116.06956652027164 39.06419409761567, 115.89667971961711 39.15660401782441, 115.74514317145017 39.28096684894041, 115.62078034033418 39.432503397107354, 115.52837042012543 39.605390197761864, 115.4714646722335 39.79298330811083, 115.45224995263672 39.988073630126955, 115.4714646722335 40.18316395214308, 115.52837042012543 40.370757062492046, 115.62078034033418 40.543643863146556, 115.74514317145017 40.6951804113135, 115.89667971961713 40.8195432424295, 116.06956652027164 40.91195316263824, 116.25715963062059 40.96885891053019, 116.45224995263672 40.988073630126955, 116.64734027465285 40.96885891053019, 116.83493338500182 40.91195316263824, 117.00782018565633 40.819543242429496, 117.15935673382327 40.6951804113135, 117.28371956493926 40.543643863146556, 117.37612948514801 40.37075706249204, 117.43303523303994 40.183163952143076, 117.45224995263672 39.988073630126955))");
		//5公里矩形
//		m.put("the_geom", "Polygon((116.40733418843074 39.94315786592098,116.40733418843074 40.03298939433293,116.4971657168427 40.03298939433293,116.4971657168427 39.94315786592098,116.40733418843074 39.94315786592098))");
		//5.5公里矩形
//		m.put("the_geom", "Polygon((116.40284261201015 39.93866628950038,116.40284261201015 40.03748097075353,116.50165729326329 40.03748097075353,116.50165729326329 39.93866628950038,116.40284261201015 39.93866628950038))");
		//5km半径圆形
//		m.put("the_geom", "POLYGON ((116.50224995263672 39.988073630126955, 116.50128921665689 39.99782814622776, 116.49844392926228 40.00720780174521, 116.49382343325185 40.015852141777934, 116.48760529169604 40.023428969186284, 116.4800284642877 40.02964711074208, 116.47138412425497 40.03426760675252, 116.46200446873753 40.037112894147114, 116.45224995263672 40.03807363012695, 116.4424954365359 40.037112894147114, 116.43311578101847 40.03426760675252, 116.42447144098574 40.02964711074208, 116.4168946135774 40.023428969186284, 116.41067647202159 40.015852141777934, 116.40605597601116 40.00720780174521, 116.40321068861655 39.99782814622776, 116.40224995263672 39.988073630126955, 116.40321068861655 39.97831911402615, 116.40605597601116 39.9689394585087, 116.41067647202159 39.960295118475976, 116.4168946135774 39.952718291067626, 116.42447144098574 39.94650014951183, 116.43311578101847 39.94187965350139, 116.4424954365359 39.939034366106796, 116.45224995263672 39.93807363012696, 116.46200446873753 39.939034366106796, 116.47138412425497 39.94187965350139, 116.4800284642877 39.94650014951183, 116.48760529169604 39.952718291067626, 116.49382343325185 39.960295118475976, 116.49844392926228 39.9689394585087, 116.50128921665689 39.97831911402615, 116.50224995263672 39.988073630126955))");
		//大于100平矩形
//		m.put("the_geom", "Polygon((116.4263491870 39.9612730692,116.4272160669 40.0167533806,116.4902093372 40.0627043990,116.4740275797 39.9505815508,116.4263491870 39.9612730692))");
		//大于100平外扩矩形
		//m.put("the_geom", "Polygon((116.39002163619203 39.93251256450497,116.39002163619203 40.06751138540245,116.52502045708952 40.06751138540245,116.52502045708952 39.93251256450497,116.39002163619203 39.93251256450497))");
		m.put("the_geom", "Polygon((118.59749794006348 24.942913055419922,118.59749794006348 24.960250854492188,118.61483573913574 24.960250854492188,118.61483573913574 24.942913055419922,118.59749794006348 24.942913055419922))");
		//小于100平外扩矩形
//		m.put("the_geom", "POLYGON((116.405641827287 39.9360848675669,116.405641827287 40.0348995488201,116.504456508541 40.0348995488201,116.504456508541 39.9360848675669,116.405641827287 39.9360848675669))");
		//polygon ((0 0, 0 10, 10 10, 10 0, 0 0))
//		m.put("the_geom", "Polygon((116.4056418272874 39.936084867566926,116.4056418272874 40.03489954882007,116.50445650854054 40.03489954882007,116.50445650854054 39.936084867566926,116.4056418272874 39.936084867566926))");
//		m.put("the_geom", "POLYGON((123.549505926594 45.1995059265937,123.549505926594 45.2004940734063,123.550494073406 45.2004940734063,123.550494073406 45.1995059265937,123.549505926594 45.1995059265937))");
		m.put("province", "中国");
		m.put("aoi_name", "中国");
		map.add(m);
//		Map m1 = new HashMap();
//		m1.put("contract_no", "22");
//		m1.put("sceneno", "222");
//		m1.put("order_no", "222");
//		m1.put("aoi_area", "222");
//		m1.put("the_geom", "POLYGON ((128.55 45.2, 128.52592363336097 45.6900857016478, 128.45392640201615 46.17545161008064, 128.33470167866105 46.65142338627231, 128.16939766255643 47.11341716182545, 127.95960632174177 47.55698368412999, 127.70734806151272 47.977851165098016, 127.41505226681369 48.37196642081823, 127.08553390593273 48.73553390593274, 126.72196642081822 49.06505226681369, 126.32785116509801 49.35734806151273, 125.90698368412998 49.60960632174178, 125.46341716182545 49.81939766255644, 125.00142338627231 49.98470167866105, 124.52545161008064 50.10392640201616, 124.0400857016478 50.17592363336099, 123.55 50.2, 123.0599142983522 50.17592363336099, 122.57454838991936 50.10392640201616, 122.09857661372769 49.98470167866105, 121.63658283817455 49.81939766255644, 121.19301631587001 49.60960632174178, 120.77214883490198 49.35734806151273, 120.37803357918177 49.06505226681369, 120.01446609406726 48.73553390593274, 119.6849477331863 48.37196642081823, 119.39265193848728 47.977851165098016, 119.14039367825822 47.55698368412999, 118.93060233744356 47.11341716182545, 118.76529832133895 46.65142338627231, 118.64607359798384 46.17545161008064, 118.57407636663902 45.69008570164781, 118.55 45.2, 118.57407636663902 44.7099142983522, 118.64607359798384 44.22454838991936, 118.76529832133895 43.74857661372769, 118.93060233744356 43.28658283817455, 119.14039367825822 42.84301631587002, 119.39265193848728 42.42214883490199, 119.6849477331863 42.02803357918177, 120.01446609406726 41.664466094067265, 120.37803357918176 41.33494773318632, 120.77214883490198 41.042651938487275, 121.19301631587001 40.790393678258226, 121.63658283817455 40.58060233744357, 122.09857661372769 40.41529832133896, 122.57454838991936 40.29607359798385, 123.0599142983522 40.22407636663902, 123.55 40.2, 124.0400857016478 40.22407636663902, 124.52545161008064 40.29607359798385, 125.00142338627231 40.41529832133896, 125.46341716182545 40.58060233744357, 125.90698368412998 40.790393678258226, 126.32785116509801 41.042651938487275, 126.72196642081822 41.33494773318632, 127.08553390593273 41.664466094067265, 127.41505226681367 42.02803357918177, 127.70734806151272 42.42214883490199, 127.95960632174177 42.84301631587002, 128.16939766255643 43.28658283817455, 128.33470167866105 43.74857661372769, 128.45392640201615 44.224548389919356, 128.52592363336097 44.7099142983522, 128.55 45.2))");
//		m1.put("province", "中国2");
//		m1.put("aoi_name", "中国2");
//		map.add(m1);
		
		
		File file = new File("1231");
		if(!file.exists()){
			file.mkdirs();
		}
		File f = new File("大于100平外扩.shp");
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ShpReadUtils.write(f,map,"YPS");
		
	}
	
	/**
	 * 读shp文件（图形信息+属性信息）的写法
	 * 
	 * @param filepath
	 */
	public void readShpGIAI(String filepath) {
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		try {
			ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory
					.createDataStore(new File(filepath).toURI().toURL());
			sds.setCharset(Charset.forName("UTF-8"));
			SimpleFeatureSource featureSource = sds.getFeatureSource();
			SimpleFeatureType schema = featureSource.getSchema();
			Style style = SLD.createSimpleStyle(featureSource.getSchema());
			SimpleFeatureIterator itertor = featureSource.getFeatures()
					.features();

			while (itertor.hasNext()) {
				SimpleFeature feature = itertor.next();
				Iterator<Property> it = feature.getProperties().iterator();

				while (it.hasNext()) {
					Property pro = it.next();
					PropertyDescriptor descriptor = pro.getDescriptor();
					Name name = pro.getName();
					PropertyType type = pro.getType();
					Map<Object, Object> userData = pro.getUserData();
					Object value = pro.getValue();
					System.out.println(pro);
				}
			}
			itertor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读图形信息
	 * 
	 * @param filepath
	 */
	public void readShpGI(String filepath) {
		try {
			ShpFiles sf = new ShpFiles(filepath);
			ShapefileReader r = new ShapefileReader(sf, false, false,
					new GeometryFactory());
			while (r.hasNext()) {
				Geometry shape = (Geometry) r.nextRecord().shape(); // com.vividsolutions.jts.geom.Geometry;
				System.out.println(shape.toString());
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读dbf文件
	 * 
	 * @param filepath
	 */
	public void readDBF(String filepath) {
		try {
			FileChannel in = new FileInputStream(filepath).getChannel();
			DbaseFileReader dbfReader = new DbaseFileReader(in, false,
					Charset.forName("UTF-8"));
			DbaseFileHeader header = dbfReader.getHeader();
			int fields = header.getNumFields();

			while (dbfReader.hasNext()) {
				DbaseFileReader.Row row = dbfReader.readRow();
				// System.out.println(row.toString());
				for (int i = 0; i < fields; i++) {
					System.out.println(header.getFieldName(i) + " : "
							+ row.read(i));
				}
			}
			dbfReader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写shape文件
	 *
	 */
	public static void write(File file,List<Map> map,String flag) {
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			// 定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", MultiPolygon.class);
			tb.add("OrderNo", String.class);
			tb.add("SubOrderNo", String.class);
			tb.add("PROVINCE", String.class);
			tb.add("AOI_AREA", String.class);
			if(flag.equals("YPS")){
				tb.add("ID", String.class);
				tb.add("Ful_AREA", String.class);
			}else if(flag.equals("KBQ")){
				tb.add("UnFul_AREA", String.class);
			}else if(flag.equals("DGMJ")){
				tb.add("Category", String.class);
			}else{
				tb.add("ID", String.class);
			}
			tb.add("AOI_NAME", String.class);
			ds.createSchema(tb.buildFeatureType());
			ds.setCharset(Charset.forName("GBK"));
			// 设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0],Transaction.AUTO_COMMIT);
			// 写下一条
			SimpleFeature feature;
			for(int i=0;i<map.size();i++){
				feature = writer.next();
				feature.setAttribute("the_geom", map.get(i).get("the_geom"));
				if(map.get(i).get("sceneno")!=null){
					//[{province=海南省, aoi_name=AOI_louyou_hainan_64, sceneno=, contract_no=8109190012, aoi_area=64.00, order_no=8109190012001, the_geom=MULTIPOLYGON(((109.16144439282 18.1196512635921,109.23855560718 18.1196512635921,109.23855560718 18.0463487364079,109.16144439282 18.0463487364079,109.16144439282 18.1196512635921)))}]
					feature.setAttribute("ID", map.get(i).get("sceneno"));
				}
				if(map.get(i).get("contract_no")!=null){
					feature.setAttribute("OrderNo", map.get(i).get("contract_no"));
				}
				if(map.get(i).get("Ful_AREA")!=null){
					feature.setAttribute("Ful_AREA", map.get(i).get("Ful_AREA"));
				}
				if(map.get(i).get("UnFul_AREA")!=null){
					feature.setAttribute("UnFul_AREA", map.get(i).get("UnFul_AREA"));
				}
				if(map.get(i).get("order_no")!=null){
					feature.setAttribute("SubOrderNo", map.get(i).get("order_no"));
				}
				if(map.get(i).get("category")!=null){
					feature.setAttribute("Category", map.get(i).get("category"));
				}
				feature.setAttribute("PROVINCE", map.get(i).get("province"));
				feature.setAttribute("AOI_AREA", map.get(i).get("aoi_area"));
				feature.setAttribute("AOI_NAME", map.get(i).get("aoi_name"));
			}
			writer.write();
			writer.close();
			ds.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 写shape文件
	 *
	 */
	public static void writeOg(File file,List<Map> map) {
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			// 定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", MultiPolygon.class);
			tb.add("Aoi_name", String.class);
			tb.add("OrderNo", String.class);
			tb.add("Satellite", String.class);
			tb.add("Stripid", String.class);
			tb.add("Shoot_time", String.class);
			tb.add("Cloud", String.class);
			tb.add("AOICloud", String.class);
			tb.add("Resolution", String.class);
			tb.add("Angle", String.class);
			tb.add("SolarAngle", String.class);
			ds.createSchema(tb.buildFeatureType());
			ds.setCharset(Charset.forName("GBK"));
			// 设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0],Transaction.AUTO_COMMIT);
			// 写下一条
			SimpleFeature feature;
			for(int i=0;i<map.size();i++){
				feature = writer.next();
				feature.setAttribute("the_geom", map.get(i).get("order_aoi"));
				feature.setAttribute("Aoi_name", map.get(i).get("aoiname"));
				feature.setAttribute("OrderNo", map.get(i).get("no"));
				feature.setAttribute("Satellite", map.get(i).get("satellite"));
				feature.setAttribute("Stripid", map.get(i).get("stripid"));
				feature.setAttribute("Shoot_time", map.get(i).get("shootingtime"));
				feature.setAttribute("Cloud", map.get(i).get("stripcloudcover"));
				feature.setAttribute("AOICloud", map.get(i).get("aoi_cloud_cover"));
				feature.setAttribute("Resolution", map.get(i).get("resolution"));
				feature.setAttribute("Angle", map.get(i).get("angle"));
				feature.setAttribute("SolarAngle", map.get(i).get("solaraltitude"));
			}
			writer.write();
			writer.close();
			ds.dispose();

			// 读取刚写完shape文件的图形信息
//			ShpFiles shpFiles = new ShpFiles(filepath+"/aoi_4_7_17_100.shp");
//			ShapefileReader reader = new ShapefileReader(shpFiles, false, true,
//					new GeometryFactory(), false);
//			try {
//				while (reader.hasNext()) {
//					System.out.println(reader.nextRecord().shape());
//				}
//			} finally {
//				reader.close();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 写shape文件
	 * flag 合格不合格标识
	 */
	public static void writeShpFlag(File file,List<Map> map) {
		try {
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
			// 定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			tb.add("the_geom", MultiPolygon.class);
			tb.add("Aoi_name", String.class);
			tb.add("OrderNo", String.class);
			tb.add("SubOrderNo", String.class);
			tb.add("Satellite", String.class);
			tb.add("Stripid", String.class);
			tb.add("Shoot_time", String.class);
			tb.add("Cloud", String.class);
			tb.add("AOICloud", String.class);
			tb.add("Resolution", String.class);
			tb.add("Angle", String.class);
			tb.add("SolarAngle", String.class);
			tb.add("Flag", String.class);
			ds.createSchema(tb.buildFeatureType());
			ds.setCharset(Charset.forName("GBK"));
			// 设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0],Transaction.AUTO_COMMIT);
			// 写下一条
			SimpleFeature feature;
			for(int i=0;i<map.size();i++){
				feature = writer.next();
				feature.setAttribute("the_geom", map.get(i).get("order_aoi"));
				feature.setAttribute("Aoi_name", map.get(i).get("aoiname"));
				feature.setAttribute("OrderNo", map.get(i).get("cNo"));
				feature.setAttribute("SubOrderNo", map.get(i).get("no"));
				feature.setAttribute("Satellite", map.get(i).get("satellite"));
				feature.setAttribute("Stripid", map.get(i).get("stripid"));
				feature.setAttribute("Shoot_time", map.get(i).get("shootingtime"));
				feature.setAttribute("Cloud", map.get(i).get("stripcloudcover"));
				feature.setAttribute("AOICloud", map.get(i).get("aoi_cloud_cover"));
				feature.setAttribute("Resolution", map.get(i).get("resolution"));
				feature.setAttribute("Angle", map.get(i).get("angle"));
				feature.setAttribute("SolarAngle", map.get(i).get("solaraltitude"));
				feature.setAttribute("Flag", map.get(i).get("flag"));
			}
			writer.write();
			writer.close();
			ds.dispose();

			// 读取刚写完shape文件的图形信息
//			ShpFiles shpFiles = new ShpFiles(filepath+"/aoi_4_7_17_100.shp");
//			ShapefileReader reader = new ShapefileReader(shpFiles, false, true,
//					new GeometryFactory(), false);
//			try {
//				while (reader.hasNext()) {
//					System.out.println(reader.nextRecord().shape());
//				}
//			} finally {
//				reader.close();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 由源shape文件创建新的shape文件
	 * 
	 * @param srcfilepath
	 * @param destfilepath
	 */
	public void transShape(String srcfilepath, String destfilepath) {
		try {
			// 源shape文件
			ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory()
					.createDataStore(new File(srcfilepath).toURI().toURL());
			// 创建目标shape文件对象
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
			params.put(ShapefileDataStoreFactory.URLP.key, new File(
					destfilepath).toURI().toURL());
			ShapefileDataStore ds = (ShapefileDataStore) factory
					.createNewDataStore(params);
			// 设置属性
			SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS
					.getTypeNames()[0]);
			// 下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
			ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(),
					DefaultGeographicCRS.WGS84));

			// 设置writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
					.getFeatureWriter(ds.getTypeNames()[0],
							Transaction.AUTO_COMMIT);

			// 写记录
			SimpleFeatureIterator it = fs.getFeatures().features();
			try {
				while (it.hasNext()) {
					SimpleFeature f = it.next();
					SimpleFeature fNew = writer.next();
					fNew.setAttributes(f.getAttributes());
					writer.write();
				}
			} finally {
				it.close();
			}
			writer.close();
			ds.dispose();
			shapeDS.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * 读取shap格式的文件
	 *  
	 * @param path 
	 * @return 
	 */  
	public static List<Map<Object, Object>> readSHP(String path,String code) {
	    ShapefileDataStore shpDataStore = null;  
	    try {  
	        shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());  
	        shpDataStore.setCharset(Charset.forName(code));  
	        //文件名称
	        String typeName = shpDataStore.getTypeNames()[0];  
	        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;  
	        featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);  
	        FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();  
	        SimpleFeatureType schema = result.getSchema(); // schema  
	        List<AttributeDescriptor> columns = schema.getAttributeDescriptors();  
	        FeatureIterator<SimpleFeature> itertor = result.features();  
	        /* 
	         * 或者使用 FeatureReader FeatureReader reader = 
	         * DataUtilities.reader(result); while(reader.hasNext()){ 
	         * SimpleFeature feature = (SimpleFeature) reader.next(); } 
	         */  
	        List<Map<Object,Object>> dataList = new ArrayList<Map<Object,Object>>();
	        while (itertor.hasNext()) {  
	            SimpleFeature feature = itertor.next();  
	            Map<Object,Object> data = new HashMap<Object, Object>();
	            Map<String,Object> info1 = new HashMap<String, Object>();
	            Map<String,Object> info2 = new HashMap<String, Object>();
	            for (AttributeDescriptor attributeDes : columns) {
	            	String attributeName = attributeDes.getName().toString();
	            	Object attribute = feature.getAttribute(attributeName);
	            	if(attribute!=null){
						if(attribute.toString().contains("'")){
							attribute = attribute.toString().replace("'","''");
						}
					}
					if(!attributeName.equalsIgnoreCase("gid")){
						if(attributeName.equalsIgnoreCase("postpha")){
							info1.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("forwardcla")){
							info1.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("picturefir")){
							info1.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("picdesfirs")){
							info1.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("forwardpha")){
							info2.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("postcalss")){
							info2.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("picturesec")){
							info2.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("picdesseco")){
							info2.put(attributeName,attribute);
						} else if(attributeName.equalsIgnoreCase("the_geom")||attributeName.equalsIgnoreCase("geom")){
							data.put(attributeName, "SRID=4326;"+attribute);
						}else{
							data.put(attributeName, attribute);
						}
					}
	            }
	            if(info1.size()>0&&info2.size()>0){
					String json1 = JSON.toJSONString(info1);
					String json2 = JSON.toJSONString(info2);
					data.put("info1",json1);
					data.put("info2",json2);
				}

	            dataList.add(data);
	        }  
	        itertor.close();
	        shpDataStore.dispose();
	        return dataList;
	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
		return null;  
	} 
	
	/** 
	 * 读取dbf格式的文件，只存储属性值，不存储空间值 
	 *  
	 * @param path 
	 */  
	public void readDBF2(String path) {
	    DbaseFileReader reader = null;  
	    try {  
	        reader = new DbaseFileReader(new ShpFiles(path), false,  
	                Charset.forName("UTF-8"));  
	        DbaseFileHeader header = reader.getHeader();  
	        int numFields = header.getNumFields();  
	        for (int i = 0; i < numFields; i++) {  
	            header.getFieldName(i);  
	            header.getFieldType(i);// 'C','N'  
	            header.getFieldLength(i);  
	        }  
	  
	        // 迭代读取记录  
	        while (reader.hasNext()) {  
	            try {  
	                Object[] entry = reader.readEntry();  
	                for (int i = 0; i < numFields; i++) {  
	                    String title = header.getFieldName(i);  
	                    Object value = entry[i];  
	                    String name = title.toString(); // column  
	                    String info = value.toString(); // value  
	                    System.out.println("name : "+ name + " info : "+info);
	                }  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    } catch (Exception ex) {  
	        ex.printStackTrace();  
	    } finally {  
	        if (reader != null) {  
	        	// 关闭  
	            try {  
	                reader.close();  
	            } catch (Exception e) {  
	            }  
	        }  
	    }  
	}  
	
	public void readSld(String path) {
		
		//样式文件的加载
		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
		File file = new File(path);
		SLDParser stylereader;
		try {
			stylereader = new SLDParser(styleFactory, file.toURI().toURL());
			Style[] stylearray = stylereader.readXML();
			Style style = stylearray[0];
	        List<Rule> rules = style.featureTypeStyles().get(0).rules();
	        for (Rule rule : rules) {
				List<Symbolizer> symbolizers = rule.symbolizers();
				for (Symbolizer symbolizer : symbolizers) {
					PointSymbolizer pointSym = (PointSymbolizer) symbolizer;
					Graphic graphic = pointSym.getGraphic();
					Mark[] marks = graphic.getMarks();
					for (Mark mark : marks) {
						Stroke stroke = mark.getStroke();
						Expression opacity = stroke.getOpacity();
						Expression width = stroke.getWidth();
						Expression color = stroke.getColor();
						System.out.println(opacity+";" + width + ";"+ color);
					}
				}
			}
			System.out.println(style);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 以字符为单位读取文件，常用于读文本，数字等类型的文件
	 * by xqr
	 */
	public static String readFileByChars(File f) {
		String text = "";
		Reader reader = null;
		try {
			if(!f.exists()){
				text = "GBK";
				return text;
			}
			System.out.println("以字符为单位读取文件内容，一次读多个字节：");
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(f));
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							System.out.print(tempchars[i]);
							text += tempchars[i];
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return text;
	}


	/**
	 * 读取CPG文件中的编码，如果没有CPG文件，默认是UTF-8编码
	 *
	 * @param filePath
	 * @return
	 */
	public static String read_txt(String filePath) {
		File file = new File(filePath);
		BufferedReader reader = null;
		String temp = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) {
				return temp.toUpperCase();
			}
			return "UTF-8";
		} catch (Exception e) {
			temp = "UTF-8";
			e.printStackTrace();
			return temp;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
