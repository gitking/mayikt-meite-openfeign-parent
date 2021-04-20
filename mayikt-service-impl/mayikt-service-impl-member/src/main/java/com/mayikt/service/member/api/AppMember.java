package com.mayikt.service.member.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMember {
	public static void main(String[] args) {
		/*
		 * 测试集群的时候把application.yml里面的端口改一下,这个main方法再次运行一下就行了。
		 * 启动的本项目时需要先将本地的nacos服务启动起来
		 * http://localhost:8080/getUser
		 * http://localhost:8081/getUser
		 */
		SpringApplication.run(AppMember.class, args);
	}
}
