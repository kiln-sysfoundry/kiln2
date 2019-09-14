package org.sysfoundry.kiln.factory;

import lombok.NonNull;

public class ObjectInstanceFactory<T> extends SingleTypeInstanceFactory<T>{


    private T instance;

    public ObjectInstanceFactory(@NonNull InstanceFactory parent,
                                 @NonNull Class<? extends T> instanceType,
                                 @NonNull T instance
                                 ) {
        super(parent, instanceType, instanceFactory -> {
            throw new ConstructionFailedException("Something is wrong. We should not be here!!!");
        });
        this.instance = instance;
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        return (T)instance;
    }
}
