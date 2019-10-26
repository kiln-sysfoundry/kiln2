package org.sysfoundry.kiln.factory;

import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class MultimapBaseInstanceFactory extends BaseInstanceFactory{

    protected Multimap<Class,InstanceFactory> instanceFactoryMultimap;

    public MultimapBaseInstanceFactory(InstanceFactory parent,Multimap<Class,InstanceFactory> instanceFactoryMultimap) {
        super(parent);
        this.instanceFactoryMultimap = instanceFactoryMultimap;
    }

    @Override
    protected Class[] providedTypes() {
        return instanceFactoryMultimap.keySet().toArray(new Class[0]);
    }

    @Override
    public <T> Set<T> getAll(Class<? extends T> type) throws FactoryException {
        Collection<InstanceFactory> instanceFactories = instanceFactoryMultimap.get(type);
        Set<T> allItems = new LinkedHashSet<>();
        for (InstanceFactory instanceFactory : instanceFactories) {
            allItems.add(instanceFactory.get(type));
        }
        return allItems;
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        if(instanceFactoryMultimap.containsKey(type)){
            InstanceFactory[] instanceFactories = instanceFactoryMultimap.get(type).toArray(new InstanceFactory[0]);
            InstanceFactory instanceFactory =  instanceFactories[0]; //get the first item in the list
            return instanceFactory.get(type);
        }

        return (T)throwUnsupportedTypeException(type);
    }
}
