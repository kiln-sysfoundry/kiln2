package org.sysfoundry.kiln.sys;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.NonNull;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;

import java.util.Set;

public abstract class BaseSubsys<C> implements Subsys<C>{

    protected Class<? extends InstanceFactory> factoryClass;
    protected Class<? extends C> configClass;
    protected C configurationObj;
    protected InstanceFactory sysInstanceFactory;
    protected String configPath;


    public BaseSubsys(@NonNull Class<? extends InstanceFactory> factoryClass,
                      @NonNull Class<? extends C> configClass,@NonNull String configPath){
        this.factoryClass = factoryClass;
        this.configClass = configClass;
        this.configPath = configPath;
    }

    @Override
    public Class<? extends InstanceFactory> getFactoryClass() {
        return factoryClass;
    }

    @Override
    public Class<? extends C> getConfigClass() {
        return configClass;
    }

    @Override
    public String getConfigPath() {
        return configPath;
    }

    @Override
    public Config getDefaultConfig() {
        return ConfigFactory.load(getName());
    }

    @Override
    public void configure(C configuration) throws LifecycleException {
        this.configurationObj = configuration;
    }

    @Override
    public void initialize(@NonNull InstanceFactory sysFactory) throws LifecycleException {
        this.sysInstanceFactory = sysFactory;
    }

    protected <T> T lookup(Class<? extends T> clazz) throws FactoryException {
        return sysInstanceFactory.get(clazz);
    }

    protected <T> Set<T> lookupAll(Class<? extends T> clazz) throws FactoryException {
        return sysInstanceFactory.getAll(clazz);
    }

}
