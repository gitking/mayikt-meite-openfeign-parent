package com.mayikt.service.member.api.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayikt.service.member.api.MemberService;

@RestController
public class MemberServiceImpl implements MemberService{
	@Value("${server.port}")
	private String serverPort;
	
	@Override
	//@GetMapping("/getUser")这个注解没必要加了,因为在mayikt-service-api-member项目的接口MemberService里面已经加好了
	public String getUser(Integer userId) {//http://localhost:8080/getUser
		return "我是mayikt-service-impl-member项目返回的会员服务,集群端口:" + serverPort;
	}
	
	@RequestMapping("/")//访问项目首页
	public String member(HttpServletRequest request) {//http://localhost:8080/getUser
		String serverPort = request.getHeader("serverPort");
		return "this is member, 网关端口号:" + serverPort;
	}
}
