package com.repositories.investigacion.v3.access.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.repositories.investigacion.v3.api.ServiceRegistry;
import com.repositories.investigacion.v3.loader.IPropertieLoader;
import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache;
import com.repositories.investigacion.v3.utilities.processing.ThreadInvoker;

@Service
public class ServiceInvoker implements IServiceInvoker {

	private static final Logger log = LoggerFactory.getLogger(ServiceInvoker.class);

	@Autowired
	private IPropertieLoader properties;
	
	@Autowired
	private ServiceRegistry serviceRegistry;
	
	@Override
	public SynchronizedCache resultInvoker(String query) {
		log.info("Query in ServiceInvoker: {} " , query);
		
		ThreadInvoker threads = new ThreadInvoker();
		threads.initialize(query, serviceRegistry, properties.getProperties());
		SynchronizedCache result = threads.invoke();
		
		return result;
	}

}
