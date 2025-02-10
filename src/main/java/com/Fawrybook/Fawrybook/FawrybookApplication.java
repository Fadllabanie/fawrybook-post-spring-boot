package com.Fawrybook.Fawrybook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FawrybookApplication {

	public static void main(String[] args) {
		SpringApplication.run(FawrybookApplication.class, args);


	}

}
