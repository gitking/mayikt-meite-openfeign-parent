package com.mayikt.service.order.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.mayikt.service.order.openfeign.MemberServiceFeign;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderService {
	@Autowired
	private MemberServiceFeign memberServiceFeign;
	//限流规则名称
	private static final String GETORDER_KEY = "getOrder";
	/*
	 * 基于我们的openfeign客户端形式实现rpc调用
	 * Openfeign默认是支持负载均衡的,默认是ribbon实现的负载均衡
	 */
	@RequestMapping("/orderOpenFeignToMember")
	public String orderOpenFeignToMember() {
		String result = memberServiceFeign.getUser(1);//这里默认是用post方法进行远程调用的
		return "我是mayikt-service-impl-order项目订单服务通过OpenFeign远程调用会员服务的接口,返回结果为:" + result;
	}
	
	@RequestMapping("/")//访问项目首页
	public String member(HttpServletRequest request) {//http://localhost:8090/
		return "this is order, mayikt-service-impl-order";
	}
	
	@RequestMapping("/orderToMember")
	public String orderToMember() {
		Entry entry = null;
		try {
			entry = SphU.entry(GETORDER_KEY);
			return "orderToMember接口";
		} catch (BlockException e) {
			//限流的情况就会进入到Exception
			e.printStackTrace();
			return "orderToMember,当前排队人数过多,请稍候重试.";
		} finally {
			//SphU.entry(xxx)需要与entry.exit()成对出现,否则会导致调用链记录异常.这些方法可以使用
			if (entry != null) {
				entry.exit();
			}
		}
	}
	
	/*
	 * 自己去看 @SentinelResource源码里面有哪些参数可以使用
	 * fallback 服务降级执行的本地方法()
	 * blockHandler 限流/熔断出现异常执行的方法
	 * fallback跟blockHandler把服务降级,限流,熔断,接口超时发生的异常,接口出现业务异常区分的非常清楚
	 * value 指定限流的名称
	 */
	@SentinelResource(value=GETORDER_KEY, blockHandler = "getOrderQpsException")
	@RequestMapping("/orderToMemberSentinelResource")
	public String orderToMemberSentinelResource() {
		return "正常访问,orderToMemberSentinelResource";
	}
	/*
	 * blockHanlder调用的方法
	 */
	public String getOrderQpsException(BlockException e) {
		e.printStackTrace();
		return "该接口已经被限流了,orderToMemberSentinelResource";
	}
	/*
	 * 通过代码手工创建我们的限流规则
	 * 这个要你自己在浏览器上手工调用这个接口来初始化限流规则
	 */
	@RequestMapping("/initFlowQpsRule")
	public String initFlowQpsRule() {
		List<FlowRule> rules = new ArrayList<FlowRule>();
		FlowRule rule1 = new FlowRule();
		rule1.setResource(GETORDER_KEY);//限流规则名称
		//QPS阈值,控制在1
		rule1.setCount(1);
		//限流类型:通过QPS来限流
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//rule1.setGrade(RuleConstant.FLOW_GRADE_THREAD);限流类型:通过信号量(线程来限流)
		rule1.setLimitApp("default");
		rules.add(rule1);
		FlowRuleManager.loadRules(rules);
		return "...限流配置初始化成功...";
	}
	
	/*
	 * 基于我们的控制台创建规则实现限流
	 * 注意如果没有使用@SentinelResource注解的情况下默认的资源名称为接口的地址要带斜杠比如:/getOrderConsole
	 * 如果要想修改默认的英文限流提示信息,只能自己加上@SentinelResource这个注解来修改。
	 * 注意:sentinel控制台配置的限流规则默认是不会持久化的,默认都是在项目运行的内存里面的。
	 * 项目一旦关闭,控制台上面的限流规则会全部消失,如果需要持久化的话可以采用zk,nacos,携程阿波罗等.推荐nacos
	 */
	@SentinelResource(value="chineseTips", blockHandler = "getOrderConsoleQpsException")
	@RequestMapping("/getOrderConsole")
	public String getOrderConsole() {
		return "getOrderConsole接口111111";
	}
	
	@SentinelResource(value="getOrderSentinelNacosConfig", blockHandler = "getOrderConsoleQpsException")
	@RequestMapping("/getOrderSentinelNacosConfig")
	public String getOrderSentinelNacosConfig() {
		return "getOrderSentinelNacosConfig接口:->Sentinle利用Nacos做持久化了";
	}
	
	/*
	 * blockHanlder调用的方法
	 */
	public String getOrderConsoleQpsException(BlockException e) {
		e.printStackTrace();
		return "该接口已经被限流了,getOrderConsole|getOrderSemaphore|getOrderSentinelNacosConfig|22222222222";
	}
	
	
	@SentinelResource(value="getOrderSemaphore", blockHandler = "getOrderConsoleQpsException")
	@RequestMapping("/getOrderSemaphore")
	public String getOrderSemaphore() {
		try {//这里要sleep阻塞一下,如果不阻塞一下,你测试不出来,你收手工刷新一下页面,代码就执行完了,你就体会不到限流的效果了
			Thread.sleep(1000);//线程数/信号量限流
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info(">>>" + Thread.currentThread().getName());
		return "getOrderSemaphore222223333";
	}
	
	/*
	 * 基于RT模式实现熔断降级
	 * Sentinel服务降级之RT平均响应时间
	 */
	@SentinelResource(value="getOrderDowngradeRtType", fallback = "getOrderDowngradeRtTypeFallback")
	@RequestMapping("/getOrderDowngradeRtType")
	public String getOrderDowngradeRtType() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "getOrderDowngradeRtType在正常执行业务逻辑,你正在测试RT平均响应时间这个功能";
	}
	
	public String getOrderDowngradeRtTypeFallback() {
		return "服务熔断降级拉,当前服务器请求过多,请稍候重试.";
	}
	
	//基于我们错误比例实现服务降级熔断
	@SentinelResource(value="getOrderDowngradeErrorType", fallback = "getOrderDowngradeErrorTypeFallback")
	@RequestMapping("/getOrderDowngradeErrorType")
	public String getOrderDowngradeErrorType(int age) {
		int j = 1 / age;
		return "getOrderDowngradeErrorType在正常执行业务逻辑,你正在测试错误比例这个功能:j = " + j;
	}
	
	public String getOrderDowngradeErrorTypeFallback(int age) {
		return "getOrderDowngradeErrorTypeFallback服务熔断降级拉,当前服务器请求过多,请稍候重试.错误率太高了,age = " + age;
	}
	
}
