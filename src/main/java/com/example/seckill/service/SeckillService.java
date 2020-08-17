package com.example.seckill.service;

import com.example.seckill.dao.GoodsMapper;
import com.example.seckill.dao.OrderMapper;
import com.example.seckill.entity.Goods;
import com.example.seckill.redis.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.UUID;


@Service
public class SeckillService {

    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    OrderMapper orderMapper;

    static Logger logger = LoggerFactory.getLogger(SeckillService.class);

    @Transactional(rollbackFor = Exception.class)
    public void seckill(int goodsId) throws Exception {
        Jedis jedis = RedisTool.getJedis();
        final String LOCK_KEY = "seckill-lock-key";
        String STOCK_KEY = "stock";
        String cacheStock = jedis.get(STOCK_KEY);
        logger.error("cache:"+cacheStock);
        if (cacheStock == null){
            logger.error("未从缓存中查询到库存");
            return;
        }
        int stock = Integer.valueOf(cacheStock);
        if (stock > 0){
            String requestId = UUID.randomUUID().toString();
            if(RedisTool.tryLock(LOCK_KEY,requestId)){
                logger.error("获取分布式锁成功");
                try {
                    Goods goods = goodsMapper.select(goodsId);
                    if (goods != null) {
                        int version = goods.getVersion();
                        //版本号实现乐观锁 保持一致性
                       // goodsMapper.update(goodsId,version);
                        //普通更新
                        goodsMapper.update2(goodsId);
                        //int i = 1/0;
                        orderMapper.insert(goods.getId(), goods.getId(), 1);
                        jedis.set(STOCK_KEY,String.valueOf(goods.getCount()-1));
                    }else {
                        logger.error("未查询到货物信息");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    throw new Exception("");
                }finally {
                    RedisTool.unlock(LOCK_KEY,requestId);
                }
            }else {
                logger.error("获取分布式锁失败");
            }
        }else {
            logger.error("库存不足："+stock);
        }
    }
}
