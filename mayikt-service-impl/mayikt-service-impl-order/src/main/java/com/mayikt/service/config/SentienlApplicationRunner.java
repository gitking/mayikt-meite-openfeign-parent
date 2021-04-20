package com.mayikt.service.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import lombok.extern.slf4j.Slf4j;

/*
 * 项目启动的时候自动加载限流规则
 */
@Component
@Slf4j
//sentienl配合Nacos做持久化处理,你如果利用Nacos配置Sentinel的降级/限流规则,就不要再在项目里面使用public class SentienlApplicationRunner implements ApplicationRunner 这个接口来让项目启动的时候就直接加载Sentinel的降级/限流规则的规则了。
//public class SentienlApplicationRunner implements ApplicationRunner{
public class SentienlApplicationRunner{
	//限流规则名称
	private static final String GETORDER_KEY = "getOrder";
		
	//@Override
	public void run(ApplicationArguments args) throws Exception {
//		List<FlowRule> rules = new ArrayList<FlowRule>();
//		FlowRule rule1 = new FlowRule();
//		rule1.setResource(GETORDER_KEY);//限流规则名称
//		//QPS阈值,控制在1
//		rule1.setCount(1);
//		//限流类型:通过QPS来限流
//		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
//		//rule1.setGrade(RuleConstant.FLOW_GRADE_THREAD);限流类型:通过信号量(线程来限流)
//		rule1.setLimitApp("default");
//		rules.add(rule1);
//		FlowRuleManager.loadRules(rules);
//		log.info(">>>项目启动成功之后自动加载限流配置加载成功<<<");
	}
}
