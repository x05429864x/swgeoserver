package it.geosolutions.swgeoserver.Jedis;

import redis.clients.jedis.Jedis;

public class JedisMS {
    public static void main(String[] args) {
        Jedis jedis_M = new Jedis("192.168.8.254",6379);
        Jedis jedis_S = new Jedis("192.168.8.229",6379);

//        jedis_S.slaveof("192.168.8.254",6379);

        jedis_M.set("k1","v1");
        String result =  jedis_S.get("k1");
        System.out.println(result);
    }
}
