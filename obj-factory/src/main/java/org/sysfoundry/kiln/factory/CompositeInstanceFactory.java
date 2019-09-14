package org.sysfoundry.kiln.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CompositeInstanceFactory implements InstanceFactory{

    private Map<Class,Set<InstanceFactory>> classToFactorySetMap;

    public CompositeInstanceFactory(Map<Class,Object> instanceMap,
                                    Class<? extends InstanceFactory>... factoryClasses) throws FactoryException {
        try {
            classToFactorySetMap = createClassToFactorySetMap(instanceMap,factoryClasses);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new FactoryException(e);
        }
    }

    public CompositeInstanceFactory(Class<? extends InstanceFactory>... factoryClasses) throws FactoryException{
        this(new HashMap<>(),factoryClasses);
    }

    private Map<Class, Set<InstanceFactory>> createClassToFactorySetMap(Map<Class,Object> instanceMap,
                                                                        Class<? extends InstanceFactory>[] factoryClasses) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Map<Class,Set<InstanceFactory>> classToFactorySetMapInstance = new HashMap<>();
        for (Class<? extends InstanceFactory> factoryClass : factoryClasses) {
            InstanceFactory instanceFactory = createInstanceFactory(factoryClass);
            Set<Class> provisions = instanceFactory.getProvisions();
            provisions.forEach(clz->{
                Set<InstanceFactory> factories = null;
                boolean newSetObj = false;
                if(classToFactorySetMapInstance.containsKey(clz)){
                    factories = classToFactorySetMapInstance.get(clz);
                }else{
                    factories = new LinkedHashSet<>();
                    newSetObj = true;
                }
                factories.add(instanceFactory);
                if(newSetObj){
                    classToFactorySetMapInstance.put(clz,factories);
                }
            });
        }
        if(!instanceMap.isEmpty()) {
            InstanceFactory instanceFactory = createFactoryForExistingInstances(instanceMap);
            Set<Class> provisions = instanceFactory.getProvisions();
            provisions.forEach(clz->{
                Set<InstanceFactory> factories = null;
                boolean newSetObj = false;
                if(classToFactorySetMapInstance.containsKey(clz)){
                    factories = classToFactorySetMapInstance.get(clz);
                }else{
                    factories = new LinkedHashSet<>();
                    newSetObj = true;
                }
                factories.add(instanceFactory);
                if(newSetObj){
                    classToFactorySetMapInstance.put(clz,factories);
                }
            });

        }

        //process the readymade instance factories as well
        /*instanceFactoryMap.forEach((clz,instanceFactory)->{
            Set<InstanceFactory> factories = null;
            boolean newSetObj = false;
            if(classToFactorySetMapInstance.containsKey(clz)){
                factories = classToFactorySetMapInstance.get(clz);
            }else{
                factories = new LinkedHashSet<>();
                newSetObj = true;
            }
            factories.add(instanceFactory);
            if(newSetObj){
                classToFactorySetMapInstance.put(clz,factories);
            }
        });*/



        return classToFactorySetMapInstance;
    }

    private InstanceFactory createFactoryForExistingInstances(Map<Class, Object> instanceMap) {
        return new MappedObjectInstanceFactory(this,instanceMap);
    }

    private InstanceFactory createInstanceFactory(Class<? extends InstanceFactory> factoryClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //lookup the expected constructor and create it
        //if the expected constructor is unavailable throw an exception

        Constructor<? extends InstanceFactory> defaultExpectedConstructor =
                factoryClass.getConstructor(InstanceFactory.class);

        InstanceFactory childFactory = defaultExpectedConstructor.newInstance(new Object[]{this});

        //wrap it with a dynamic proxy
        InstanceFactory newProxyInstance = (InstanceFactory)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{InstanceFactory.class},
                new FactoryInvocationHandler(childFactory));

        return newProxyInstance;

    }

    @Override
    public Set<Class> getProvisions() {
        return classToFactorySetMap.keySet();
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        if(classToFactorySetMap.containsKey(type)){

            Set<InstanceFactory> factories = classToFactorySetMap.get(type);

            InstanceFactory firstFactory = factories.iterator().next();
            try {
                return firstFactory.get(type);
            }catch(Exception e){
                FactoryInvocationHandler.lookupRequestsThreadLocal.remove();
                throw new FactoryException(e);
            }

        }

        throw new UnsupportedInstanceTypeException(String.format("Class type %s is not supported by %s",type,this));
    }

    @Override
    public <T> Set<T> getAll(Class<? extends T> type) throws FactoryException {
        Set<T> allInstances = new LinkedHashSet<>();
        if(classToFactorySetMap.containsKey(type)){

            Set<InstanceFactory> factories = classToFactorySetMap.get(type);

            for (InstanceFactory factory : factories) {
                T t = factory.get(type);
                allInstances.add(t);
            }

            return allInstances;
        }

        throw new UnsupportedInstanceTypeException(String.format("Class type %s is not supported by %s",type,this));
    }
}
