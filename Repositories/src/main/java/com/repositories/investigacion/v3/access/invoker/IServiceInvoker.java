package com.repositories.investigacion.v3.access.invoker;

import com.repositories.investigacion.v3.utilities.pojo.SynchronizedCache;

public interface IServiceInvoker {

	public SynchronizedCache resultInvoker(String query);
}
