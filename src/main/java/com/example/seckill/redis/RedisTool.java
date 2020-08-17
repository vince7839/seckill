package com.example.seckill.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

@Component
public class RedisTool {

    static Logger logger = LoggerFactory.getLogger(RedisTool.class);

    static public boolean tryLock(String key,String requestId){
        Jedis jedis = getJedis();
        SetParams params = SetParams.setParams().ex(5).nx();
        String result = jedis.set(key,requestId,params);
        logger.error("tryLock:"+result);
        return "OK".equals(result);
    }

    static public Jedis getJedis(){
        return new Jedis("127.0.0.1",6379);
    }

    static public void unlock(String key,String requestId){
        Jedis jedis = getJedis();
        jedis.del(key);
    }
}
