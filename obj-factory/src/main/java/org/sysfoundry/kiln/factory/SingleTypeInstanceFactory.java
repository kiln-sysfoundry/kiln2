package org.sysfoundry.kiln.factory;

import lombok.NonNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class SingleTypeInstanceFactory<T> implements InstanceFactory{

    protected Class<? extends T> instanceType;
    protected ConstructorFn<T> constructorFn;
    protected Set<Class> provisions;
    protected InstanceFactory parent;

    public SingleTypeInstanceFactory(@NonNull InstanceFactory parent,
                                      @NonNull Class<? extends T> instanceType,
                                                 @NonNull ConstructorFn<T> constructorFn){
        this.parent = parent;
        this.instanceType = instanceType;
        this.constructorFn = constructorFn;
        this.provisions = new HashSet<>();
        provisions.add(instanceType);
    }

    @Override
    public Set<Class> getProvisions() {
        return provisions;
    }


    @Override
    public <T> Set<T> getAll(Class<? extends T> type) throws FactoryException {
        T t = get(type);
        Set<T> allItems = new LinkedHashSet<>();
        allItems.add(t);
        return allItems;
    }
}
