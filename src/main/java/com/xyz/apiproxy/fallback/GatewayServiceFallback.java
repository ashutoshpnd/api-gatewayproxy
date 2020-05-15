package com.xyz.apiproxy.fallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.xyz.apiproxy.mail.EmailService;

@Component
class GatewayServiceFallback implements FallbackProvider {

	private static final String DEFAULT_MESSAGE = "Service not available.";
	@Autowired
	private EmailService emailService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String getRoute() {
		return "*"; // or return null;
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		emailService.sendSimpleMessage("ashutoshcpr@gmail.com", "Employee Service Exception", cause.getMessage());
		String errMSG = null;
		try {
			errMSG = objectMapper.writeValueAsString(
					new GatewayResponse(HttpStatus.GATEWAY_TIMEOUT.name(), DEFAULT_MESSAGE, cause.getMessage()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (cause instanceof HystrixTimeoutException) {
			return new GatewayClientResponse(HttpStatus.GATEWAY_TIMEOUT, DEFAULT_MESSAGE, errMSG);
		} else {
			return new GatewayClientResponse(HttpStatus.INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE, errMSG);
		}
	}

}
