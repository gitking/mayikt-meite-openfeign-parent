package com.mayikt.service.member.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface MemberService {
	@GetMapping("/getUser")
	String getUser(@RequestParam("userId")Integer userId);
}
