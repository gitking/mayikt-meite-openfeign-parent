package com.mayikt.service.order.impl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;

import lombok.extern.slf4j.Slf4j;
/**
 * SeckillService秒杀接口,第11章 » 课时105 秒杀接口整合sentinel实现热词限流
 * https://edu.aliyun.com/lesson_2007_18316?spm=5176.8764728.0.0.1b186e929onDVK#_18316
 * @author dell
 */
@RestController
@Slf4j
public class SeckillService {
	/*
	 * 由于SeckillService是上面添加了@RestController,所以SpringBoot会在项目启动的时候就通过代理实例这个SeckillService类
	 * 当SpringBoot通过反射调用SeckillService类的无参构造方法时,我们让无参构造方法执行我们指定的initSeckillRule方法。
	 * 这就想当于initSeckillRule这个方法在项目启动的时候就执行了。
	 */
	public SeckillService() {
		//initSeckillRule();
	}
	private static final String SEKILL_RULE = "seckill";//秒杀的限流规则
//热词限流规则写死在代码里面不好,要配置到sentinel的控制台里面去,这样我们可以动态的修改热词限流规则
//	private void initSeckillRule() {
//		ParamFlowRule rule = new ParamFlowRule(SEKILL_RULE)
//				.setParamIdx(0)//对我们秒杀接口第0个参数实现限流
//				.setGrade(RuleConstant.FLOW_GRADE_QPS)
//				.setCount(1);//每秒QPS最多只有1S
//		ParamFlowRuleManager.loadRules(Collections.singletonList(rule));//将规则加载到内存当中去
//		log.info(">>>秒杀接口限流策略配置成功,我会在SpringBoot项目启动的时候被加载<<<");
//	}
	//http://localhost:8090/seckill?userId=10&orderId=99 访问这个地址,在一秒之内快速刷新就可以测试了.
	@RequestMapping("/seckill")
	public String seckill(Long userId, Long orderId) {
		try {
			//启用Sentinel热词限流的功能
			Entry entry = SphU.entry(SEKILL_RULE, EntryType.IN, 1, userId);
			//SphU.entry方法最后一个参数是可变参数,可以多传几个
			//Entry entrySec = SphU.entry(SEKILL_RULE, EntryType.IN, 1, userId, orderId);
			return "伪代码|秒杀成功";
		} catch (Exception e) {
			e.printStackTrace();
			return "用户:" + userId + "访问频率太快了,请稍候重试.";
		}
	}
	
	//http://localhost:8090/seckillConfig?userId=10&orderId=99 访问这个地址,在一秒之内快速刷新就可以测试了.
	@RequestMapping("/seckillConfig")
	@SentinelResource(value=SEKILL_RULE, fallback="seckillConfigFallback", blockHandler="seckillConfigBlockHandler")
	public String seckillConfig(Long userId, Long orderId) {
		return "伪代码|秒杀成功";
	}
	
	/*
	 * @SentinelResource的fallback和blockHandler没有用,似乎不起作用.这个应该是一个小bug
	 * 最终靠全局捕获异常解决的,参见这个类InterfaceExceptionHandler
	 */
	public String seckillConfigFallback(Long userId, Long orderId) {
		return "Sentinel你热词限流了_seckillConfigFallback";
	}
	
	public String seckillConfigBlockHandler(Long userId, Long orderId) {
		return "Sentinel你热词限流了_seckillConfigBlockHandler";
	}
}
