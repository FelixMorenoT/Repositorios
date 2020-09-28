package com.repositories.investigacion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.repositories.investigacion.processing.SynchronizedCache;
import com.repositories.investigacion.processing.ThreadInvoker;

@SpringBootApplication
@EnableZuulProxy
public class RepositoriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepositoriesApplication.class, args);        
	}
}
