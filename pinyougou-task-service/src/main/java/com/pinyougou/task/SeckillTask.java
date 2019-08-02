package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.pojo.TbSeckillGoodsExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SeckillTask {

	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	
	@Scheduled(cron="0 * * * * ?")
	public void refreshSeckillGoods(){
		System.out.println("执行了秒杀商品增量更新 任务调度"+new Date());
		
		//查询缓存中的秒杀商品ID集合
		List goodsIdList =  new ArrayList( redisTemplate.boundHashOps("seckillGoods").keys());
		System.out.println(goodsIdList);
		
		TbSeckillGoodsExample example=new TbSeckillGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");// 审核通过的商品
		criteria.andStockCountGreaterThan(0);//库存数大于0
		criteria.andStartTimeLessThanOrEqualTo(new Date());//开始日期小于等于当前日期
		criteria.andEndTimeGreaterThanOrEqualTo(new Date());//截止日期大于等于当前日期
		
		if(goodsIdList.size()>0){
			criteria.andIdNotIn(goodsIdList);//排除缓存中已经存在的商品ID集合
		}
				
		List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
		//将列表数据装入缓存 
		for(TbSeckillGoods seckillGoods:seckillGoodsList){
			redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
			System.out.println("增量更新秒杀商品ID:"+seckillGoods.getId());
		}	
		System.out.println(".....end....");
		
	}
	
	
	
	@Scheduled(cron="* * * * * ?")
	public void removeSeckillGoods(){
		//查询出缓存中的数据，扫描每条记录，判断时间，如果当前时间超过了截止时间，移除此记录
		List<TbSeckillGoods> seckillGoodsList= redisTemplate.boundHashOps("seckillGoods").values();
		System.out.println("执行了清除秒杀商品的任务"+new Date());
		for(TbSeckillGoods seckillGoods :seckillGoodsList){
			if(seckillGoods.getEndTime().getTime() < System.currentTimeMillis() ){
				//同步到数据库
				seckillGoodsMapper.updateByPrimaryKey(seckillGoods);				
				//清除缓存
				redisTemplate.boundHashOps("seckillGoods").delete(seckillGoods.getId());
				System.out.println("秒杀商品"+seckillGoods.getId()+"已过期");
								
			}			
		}		
		System.out.println("执行了清除秒杀商品的任务...end");
	}

//	private Integer i = 1;
//	@Scheduled(cron ="* * * * * ?")
//	public void Task() {
//		System.out.println("我在数绵羊"+i++);
		//秒 分 时 一月中的第x天 月 一周中的第x天 年
		// * 模糊
		// - 范围
		// ， 枚举
		// / 从起始时间开始出发，每隔一定时间执行一次。5/20 从第5分钟开始，每20分钟执行一次
		//0 0 10,14,16 * * ? 每天的10.14.16点执行一次
		//0 0/30 9-17 * * ? 每天的9点到17点，每半小时
		//0 0 12 ? * WED	每周三的中午12点
		//0 0 12 * * ?		每天的中午12点
		//0 15 10 ? * *		每天10点15分
		//0 15 10 * * ?		每天的10点15分
		//0 15 10 * * ? *		每天的10点15分
		//0 15 10 * * ? 2005	2005年的每天10点15分
		//0 * 14 * * ?			每天的14点的每分钟
		//0 0/5 14 * * ?		每天的14点0分开始，5分钟执行一次
		//0 0/5 14,18 * * ?"	每天的14点18点，从0分开始，5分钟执行一次
		//0 0-5 14 * * ?		每天的14点0-5分，每分钟执行一次
		//0 10,44 14 ? 3 WED	3月的每周的周三，14点10分和44分，各执行 一次
		//0 15 10 ? * MON-FRI	每周星期一-星期五 的10点15分
		//"0 15 10 15 * ?		每月的15日10点15分
		//0 15 10 L * ?			每月的最后一天的10点15分
		//0 15 10 ? * 6L		每月的最后一个周五10点15
		//0 15 10 ? * 6L 2002-2005	2002年-2005年，每月的最后一个周五10点15分
		//0 15 10 ? * 6#3 		每月的第三个周五10点15分

//	}
	
}
