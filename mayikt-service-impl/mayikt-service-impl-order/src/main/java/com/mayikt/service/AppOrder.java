package com.mayikt.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients//开启openFeign的功能
public class AppOrder {
	public static void main(String[] args) {
		/*
		 * 启动的本项目时需要先将本地的nacos服务启动起来(其实nacos本地服务不启动也可以)
		 * 还要先启动本地的sentinel-dashboard-1.7.1.jar服务
		 * 启动sentinel-dashboard-1.7.1.jar服务 D:\Sentinel>java -Dserver.port=8718 -Dcsp.sentinel.dashboard.server=localhost:8718 -Dproject.name=sentinel-dashboard -Dcsp.sentinel.api.port=8719 -jar sentinel-dashboard-1.7.1.jar
		 * 限流一般都是在网关做，在入口做，不会在项目的服务里面做限流
		 * Sentinel利用Nacos做持久化,在Nacos里面配置好之后,再启动项目，项目启动成功之后Sentinel的控制台上才会出现Nacos里面的配置。
		 * 项目一旦关闭Sentinel控制台里面的规则就会自动消失,因为项目启动的时候会把Nacos里面的配置读到内存里面,
		 * 然后Sentinel在启动成功之后(ApplicationRunner)再从内存里面读取配置。
		 * http://localhost:8090/orderOpenFeignToMember?userId=1
		 */
		SpringApplication.run(AppOrder.class, args);
	}
}
