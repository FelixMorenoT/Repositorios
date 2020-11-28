package com.repositories.investigacion.v3.access.heartbeat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.repositories.investigacion.v3.connection.IConnectionService;
import com.repositories.investigacion.v3.loader.IPropertieLoader;
import com.repositories.investigacion.v3.loader.PropertieLoader;
import com.repositories.investigacion.v3.utilities.pojo.Repository;

@Service
@Configuration
@EnableScheduling
public class HeartBeat implements SchedulingConfigurer, IHeartBeat{

	private static final Logger log = LoggerFactory.getLogger(PropertieLoader.class);

	@Value("${heart.beat}")
	private String timeStamp;
	
	@Autowired
	private IPropertieLoader propertieLoader;
	
	@Autowired
	private IConnectionService connection;
	
	
	private Map<String, Repository> hashProperties;
	
	private int counter = 0;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.setThreadNamePrefix("heartBeatThread");
		
		threadPoolTaskScheduler.initialize();
		taskMethod(threadPoolTaskScheduler);
		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
		
	}

	private void taskMethod(TaskScheduler scheduler) {
		scheduler.schedule(new Runnable() {
			
		@Override
		public void run() {
					
			try {
				hashProperties = propertieLoader.getProperties();
				if(counter == 0) {
					log.info("Next call heart beat  in {} milisecond" , 100);
					Thread.sleep(100);
					counter ++;
				}else {
					log.info("Next call heart beat in {} milisecond" , timeStamp);
					Thread.sleep(Long.parseLong(timeStamp));
				}
				
				System.gc();
				serverHeartBeat();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		}, new Trigger() {
			
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				String cronExp = "0/1 * * * * ?";
				return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
			}
		});
	}

	@Override
	public void serverHeartBeat() {
		hashProperties = propertieLoader.getProperties();
		for (Entry<String, Repository> entry : hashProperties.entrySet()) {
		    String key = entry.getKey();
		    Repository value = entry.getValue();
		    HttpURLConnection resultHeartBeat = connection.startConnection(value.getUrl() + value.getId() + "/?apiKey=" + value.getKey() +"&query=all(test)&count=1", true);
		    		
            int responseCode = 0;
			try {
				responseCode = resultHeartBeat.getResponseCode();
				log.info("URL: {}" , value.getUrl() + value.getId() + "/?apiKey=" + value.getKey() +"&query=all(test)&count=1");
				log.info("Repository: {} - Response Code: {}" ,value.getId(), responseCode);
				resultHeartBeat.disconnect();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
            
            if(200 <= responseCode && responseCode <= 400) {
            	value.setStatus(true);
            }else {
            	value.setStatus(false);
            }
            
            hashProperties.computeIfPresent(key, (k,v)-> value);
		}
		propertieLoader.reload();
	}
	
	public Map<String, Repository> getHashProperties() {
		return hashProperties;
	}
}
