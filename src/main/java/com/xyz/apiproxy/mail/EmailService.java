package com.xyz.apiproxy.mail;

public interface EmailService {
	public void sendSimpleMessage(String to, String subject, String text);
}
