package org.sysfoundry.kiln.factory;


import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BaseInstanceFactory implements InstanceFactory{

    protected InstanceFactory parentFactory;

    public BaseInstanceFactory(InstanceFactory parent){
        this.parentFactory = parent;
    }


    protected <T> T lookup(Class<? extends T> type) throws FactoryException {
        return parentFactory.get(type);
    }

    @Override
    public Set<Class> getProvisions() {
        return new ImmutableSet.Builder<Class>()
                .addAll(Arrays.asList(providedTypes()))
                .build();
    }

    protected abstract Class[] providedTypes();

    @Override
    public <T> Set<T> getAll(Class<? extends T> type) throws FactoryException {
        T t = get(type);
        Set<T> allItems = new LinkedHashSet<>();
        allItems.add(t);
        return allItems;
    }

    protected Object throwUnsupportedTypeException(Class typ) throws UnsupportedInstanceTypeException{
        throw new UnsupportedInstanceTypeException(String.format("Class type %s is not supported by %s",
                typ,this));
    }
}
