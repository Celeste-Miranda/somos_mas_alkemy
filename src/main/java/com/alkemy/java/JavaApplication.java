package com.alkemy.java;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class JavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	@Bean
	public ProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}

}
