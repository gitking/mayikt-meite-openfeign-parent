package com.mayikt.service.order.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderTempService {
	
	@RequestMapping("/orderToMemberTemp")
	public String orderToUserMember() {
		try {
			//模拟业务代码调用其他接口
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info(">>>" + Thread.currentThread().getName() + "<<<");
		return "订单调用会员成功";
	}
	
	@RequestMapping("/smsOrder")
	public String smsOrder() {
		log.info(">>>" + Thread.currentThread().getName() + "<<<");
		return "订单发送短信消息...";
	}
}
