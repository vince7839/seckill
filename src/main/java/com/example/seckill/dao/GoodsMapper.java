package com.example.seckill.dao;

import com.example.seckill.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMapper extends Mapper {

    @Update("update goods set count = count-1,version = version+1 where id=#{id} and #{version} = version")
    void update(int id,int version);

    @Update("update goods set count = count-1 where id=#{id}")
    void update2(int id);

    @Select("select * from goods where id = #{id}")
    Goods select(int id);
}
