package com.mayikt.service.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;

//全局捕获Sentinel的热词限流异常ParamFlowException,然后返回给用户一个友好的提示.
@RestControllerAdvice
public class InterfaceExceptionHandler {
	@ResponseBody
	@ExceptionHandler(ParamFlowException.class)
	public String businessInterfaceException(ParamFlowException e) {
		return "Sentinel的热词限流异常ParamFlowException,您访问的频率太快了,请稍候重试.";
	}
}
