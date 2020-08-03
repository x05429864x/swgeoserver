package it.geosolutions.swgeoserver.Jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;

public class JedisTransaction {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.8.254",6379);
        Transaction transaction = jedis.multi();
        String[] strs = new String[10];
        for(int i=0;i<strs.length;i++){
            strs[i] = ""+i;
        }
        transaction.lpush("listpush11",strs);
        transaction.lpush("listpush11","1111","1111");

//        transaction.exec();
        //取消事务
        transaction.discard();
    }
}
