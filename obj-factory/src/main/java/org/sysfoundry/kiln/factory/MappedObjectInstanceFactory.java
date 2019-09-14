package org.sysfoundry.kiln.factory;

import java.util.Map;

public class MappedObjectInstanceFactory extends BaseInstanceFactory{


    protected Map<Class,Object> mappedInstances;

    public MappedObjectInstanceFactory(InstanceFactory parent, Map<Class,Object> mappedInstances) {
        super(parent);
        this.mappedInstances = mappedInstances;
    }

    @Override
    protected Class[] providedTypes() {
        return mappedInstances.keySet().toArray(new Class[0]);
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        if(mappedInstances.containsKey(type)){
            return (T) mappedInstances.get(type);
        }

        return (T)throwUnsupportedTypeException(type);
    }
}
