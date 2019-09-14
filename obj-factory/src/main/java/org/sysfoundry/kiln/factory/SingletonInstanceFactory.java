package org.sysfoundry.kiln.factory;

import lombok.NonNull;

public class SingletonInstanceFactory<T> extends SingleTypeInstanceFactory<T>{


    private T instance;

    public SingletonInstanceFactory(
            @NonNull InstanceFactory parent,
            @NonNull Class<? extends T> instanceType,
                                    @NonNull ConstructorFn<T> constructorFn){
        super(parent,instanceType,constructorFn);
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        if(instance == null){
            synchronized(instanceType){
                if(instance == null){
                    try {
                        instance = constructorFn.construct(parent);
                    }catch(Exception e){
                        throw new FactoryException(e);
                    }
                }
            }
        }
        return (T)instance;
    }

}
