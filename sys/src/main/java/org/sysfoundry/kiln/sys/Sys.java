package org.sysfoundry.kiln.sys;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import lombok.NonNull;
import org.slf4j.Logger;
import org.sysfoundry.kiln.factory.CompositeInstanceFactory;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.health.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Sys {

    private Config systemConfigObj;
    private Set<Class<? extends Subsys>> subsysClassSet;
    private Set<Subsys> subsysSet;
    private Config finalSystemConfigObj;
    private InstanceFactory compositeInstanceFactory;

    public static final String NAME = "sys";

    private static final Logger log = Log.get(NAME);

    public Sys(@NonNull Config sysConfig,
               @NonNull Class<? extends Subsys>... subsysClasses){
        this.systemConfigObj = sysConfig;
        this.subsysClassSet = new LinkedHashSet<>(Arrays.asList(subsysClasses));
        try {
            this.subsysSet = createSubsysInstances(subsysClassSet);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e){
            throw new RuntimeException(e);
        }

    }

    private Set<Subsys> createSubsysInstances(Set<Class<? extends Subsys>> subsysClassSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Set<Subsys> subsysSetObj = new LinkedHashSet<>();
        for (Class<? extends Subsys> aClass : subsysClassSet) {
            Constructor<? extends Subsys> defaultConstructor = aClass.getConstructor();
            Subsys subsys = defaultConstructor.newInstance(new Object[0]);
            subsysSetObj.add(subsys);
        }

        return subsysSetObj;
    }

    private void configure()throws LifecycleException{

        finalSystemConfigObj = getMergedConfiguration();

        Map<Class,Object> mappedConfigObjects = new HashMap<>();

        for (Subsys subsys : subsysSet) {
            Class configClass = subsys.getConfigClass();
            Config subsysConfigObj = null;
            Object subsysConfig = null;
            try {
                subsysConfigObj = finalSystemConfigObj.getConfig(subsys.getConfigPath());
                subsysConfig = ConfigBeanFactory.create(subsysConfigObj,configClass);
            }catch(ConfigException.Missing missingConfigExcepton){
                log.warn("Config missing for Subsys ({}) @ {}",subsys.getName(),subsys.getConfigPath());
                try {
                    subsysConfig = configClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Exception e) {
                    log.warn("Default constructor missing for config {}, so configuration cannot be created for Subsys {}",
                            configClass,subsys.getName());

                }
            }

            subsys.configure(subsysConfig);
            mappedConfigObjects.put(configClass,subsysConfig);
        }

        Set<Class> factoryClassSet = new LinkedHashSet<>();

        for (Subsys subsys : subsysSet) {
            Class factoryClass = subsys.getFactoryClass();
            factoryClassSet.add(factoryClass);
        }

        try {
            CompositeInstanceFactory sysInstanceFactory = new CompositeInstanceFactory(mappedConfigObjects,
                    factoryClassSet.toArray(new Class[0]));
            compositeInstanceFactory = sysInstanceFactory;
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }

    }

    private Config getMergedConfiguration() {
        Config mergedConfig = ConfigFactory.load(systemConfigObj);

        for (Subsys subsys : subsysSet) {
            Config defaultConfig = subsys.getDefaultConfig();
            if(defaultConfig != null && !defaultConfig.isEmpty()){
                mergedConfig = mergedConfig.withFallback(defaultConfig);
            }
        }

        return mergedConfig;
    }

    private void initialize() throws LifecycleException{
        for (Subsys subsys : subsysSet) {
            subsys.initialize(compositeInstanceFactory);
        }

    }

    public void start() throws LifecycleException{

        configure();

        initialize();

        setupJVMShutdownHook();

        for (Subsys subsys : subsysSet) {
            subsys.start();
        }



    }

    private void setupJVMShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("JVM is shutting down. Stopping all the subsystems");
                try {
                    stop();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        },"sys-shutdown-thread"));
    }

    public void stop() throws LifecycleException{
        List<Subsys> reversedList = Arrays.asList(subsysSet.toArray(new Subsys[0]));
        Collections.reverse(reversedList);

        for (Subsys subsys : reversedList) {
            subsys.stop();
        }
    }


}
