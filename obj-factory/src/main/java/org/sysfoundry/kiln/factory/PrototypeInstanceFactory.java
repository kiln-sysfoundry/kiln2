package org.sysfoundry.kiln.factory;

import lombok.NonNull;

public class PrototypeInstanceFactory<T> extends SingleTypeInstanceFactory<T>{


    public PrototypeInstanceFactory(@NonNull InstanceFactory parent,
                                    @NonNull Class<? extends T> type,
                                    @NonNull ConstructorFn<T> constructorFn){
        super(parent,type,constructorFn);
    }


    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        T instance = null;
        try {
            instance = (T)constructorFn.construct(parent);
        }catch(Exception e){
            throw new FactoryException(e);
        }
        return instance;
    }

}
