package it.geosolutions.swgeoserver.Jedis;

import redis.clients.jedis.Jedis;

import java.util.*;

public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.8.254",6379);

//        //String
//        System.out.println("keys:"+jedis.keys("*"));
//        jedis.append("k1","v1");
//        jedis.append("k1","v2");
//        jedis.append("k1","v3");
//        jedis.set("k3","v3");
//        System.out.println("get k3:"+jedis.get("k3"));
//        System.out.println("append k1:"+jedis.get("k1")+" length:"+jedis.strlen("k1"));
//        jedis.mset("str1","v1","str2","v2","str3","v3");
//        System.out.println("mget:"+jedis.mget("str1","str2","str3"));
//
//        //List
//        jedis.lpush("list","l1","l2","l3","l4");
//        System.out.println("list llen:"+jedis.llen("list"));
//        List<String> list = jedis.lrange("list",0,jedis.llen("list"));
//        for (String str :list) {
//            System.out.println("list:"+str);
//        }
//
//
//        //Hash(map)
//        jedis.hset("hash","h1","v1");
//        System.out.println("hash hget(h1):"+jedis.hget("hash","h1"));
//        Map<String,String> map = new HashMap();
//        map.put("map1","map1 value");
//        map.put("map2","map2 value");
//        map.put("map3","map3 value");
//        jedis.hmset("maps",map);
//        List<String> hmList = jedis.hmget("maps","map1","map2");
//        for(String str:hmList){
//            System.out.println("hmList:"+str);
//        }
//
//        //Set
//        jedis.sadd("set","1");
//        jedis.sadd("set","v2");
//        jedis.sadd("set","v3");
//        jedis.sadd("set","v4");
//        Set<String> set = jedis.smembers("set");
//        Iterator<String> iterator = set.iterator();
//        while (iterator.hasNext()){
//            System.out.println("set iterator:"+iterator.next().toString());
//        }
//        //zset
//        jedis.zadd("zset01",60d,"v1");
//        jedis.zadd("zset01",70d,"v2");
//        jedis.zadd("zset01",80d,"v3");
//        jedis.zadd("zset01",90d,"v4");
//        Set<String> s1 = jedis.zrange("zset01",0,-1);
//        for(Iterator iterator = s1.iterator(); iterator.hasNext();) {
//            String string = (String) iterator.next();
//            System.out.println(string);
//        }

    }
}
