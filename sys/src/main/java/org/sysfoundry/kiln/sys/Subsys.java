package org.sysfoundry.kiln.sys;

import com.typesafe.config.Config;
import org.sysfoundry.kiln.factory.InstanceFactory;

public interface Subsys<C> {

    public Class<? extends InstanceFactory> getFactoryClass();

    public Class<? extends C> getConfigClass();

    public String getName();

    public String getConfigPath();

    public Config getDefaultConfig();

    public void configure(C configuration) throws LifecycleException;

    public void initialize(InstanceFactory sysFactory) throws LifecycleException;

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;

}
