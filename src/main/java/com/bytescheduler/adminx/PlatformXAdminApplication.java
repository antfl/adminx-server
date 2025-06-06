package com.bytescheduler.adminx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@MapperScan("com.bytescheduler.**.mapper")
public class PlatformXAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformXAdminApplication.class, args);
	}

}
