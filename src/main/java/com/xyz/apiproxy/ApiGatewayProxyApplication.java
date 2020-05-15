package com.xyz.apiproxy;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.xyz.apiproxy.security.AccountRepository;
import com.xyz.apiproxy.security.model.Account;
import com.xyz.apiproxy.security.model.Role;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@EnableZuulProxy
public class ApiGatewayProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayProxyApplication.class, args);
	}

	@Bean
	@Order(1)
	public CommandLineRunner insertUser(AccountRepository accountRepositoty) {
		return args -> Stream.of("Ashu,ashu123,true,user", "Mahesh,mahesh123,true,admin").map(tpl -> tpl.split(","))
				.forEach(tpl -> accountRepositoty.save(
						new Account(tpl[0], tpl[1], Boolean.valueOf(tpl[2])).addRole(new Role(tpl[3].toUpperCase()))));
	}

}
