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
public class EmployeeServiceFallback implements FallbackProvider {
	private static final String DEFAULT_MESSAGE = "Employee service not available";

	@Autowired
	private EmailService emailService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String getRoute() {
		return "employee-management";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		String errMSG = null;
		try {
			emailService.sendSimpleMessage("test@gmail.com", "Employee Service Exception", cause.getMessage());

		} catch (Exception ex) {
			System.out.println("Exception occured while sending the mail" + ex.getLocalizedMessage());
		}

		try {
			errMSG = objectMapper.writeValueAsString(new GatewayResponse(HttpStatus.GATEWAY_TIMEOUT.name(),
					DEFAULT_MESSAGE, cause.getLocalizedMessage()));
			System.out.println(errMSG);
		} catch (JsonProcessingException e) {
			// e.printStackTrace();
		}
		if (cause instanceof HystrixTimeoutException) {
			return new GatewayClientResponse(HttpStatus.GATEWAY_TIMEOUT, DEFAULT_MESSAGE, errMSG);
		} else {
			return new GatewayClientResponse(HttpStatus.INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE, errMSG);
		}
	}

}
