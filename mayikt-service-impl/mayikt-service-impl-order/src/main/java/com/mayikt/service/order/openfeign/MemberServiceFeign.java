package com.mayikt.service.order.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("meitemayikt-member")//这个名称是mayikt-service-impl-member项目yml配置文件里面配置的项目名称
public interface MemberServiceFeign {
	/*
	 * feign客户端如果你这里不加@RequestParam("userId")这个注解,feign默认会使用post请求来访问
	 * 但是我们这里方法上面的注解使用的是@GetMapping("/getUser"),然后就会报错说找不到getUser对应的post方法
	 * 加上@RequestParam("userId")就好了
	 * 实际不是这样的,因为在浏览器上面的访问方式不对
	 */
	@GetMapping("/getUser")
	String getUser(@RequestParam("userId")Integer userId);
}
