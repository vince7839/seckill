package com.example.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends Mapper {

    @Insert("insert into table_order(user_id,goods_id,count) values(#{userId},#{goodsId},#{count})")
    void insert(int userId,int goodsId,int count);
}
