package com.xyz.apiproxy.fallback;

public class GatewayResponse {
	
	private String status;
	private String description;
	private String error;

	public GatewayResponse(String status, String description, String error) {
		super();
		this.status = status;
		this.description = description;
		this.error = error;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
