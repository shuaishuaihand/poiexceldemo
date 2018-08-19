package com.hand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableCaching  //开启缓存注解
//@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.hand.hand.mapper")
/*@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})*/
public class HealthdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthdemoApplication.class, args);
	}
}
