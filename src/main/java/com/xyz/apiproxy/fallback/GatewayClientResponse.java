package com.xyz.apiproxy.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

public class GatewayClientResponse implements ClientHttpResponse {

	private HttpStatus status;
	private String message;
	private String errMSG;

	public GatewayClientResponse(HttpStatus status, String message, String errMSG) {
		this.status = status;
		this.message = message;
		this.errMSG = errMSG;
	}

	@Override
	public HttpStatus getStatusCode() throws IOException {
		return status;
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return status.value();
	}

	@Override
	public String getStatusText() throws IOException {

		return status.getReasonPhrase();
	}

	@Override
	public void close() {
	}

	@Override
    public InputStream getBody() throws IOException {
				return new ByteArrayInputStream(errMSG.getBytes());
    }

	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
