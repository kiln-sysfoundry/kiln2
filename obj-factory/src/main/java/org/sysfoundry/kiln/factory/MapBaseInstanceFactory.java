package org.sysfoundry.kiln.factory;

import java.util.Map;

public abstract class MapBaseInstanceFactory extends BaseInstanceFactory{

    protected Map<Class,InstanceFactory> instanceFactoryMap;

    public MapBaseInstanceFactory(InstanceFactory parent, Map<Class,InstanceFactory> instanceFactoryMap) {
        super(parent);
        this.instanceFactoryMap = instanceFactoryMap;
    }

    @Override
    protected Class[] providedTypes() {
        return instanceFactoryMap.keySet().toArray(new Class[0]);
    }

    @Override
    public <T> T get(Class<? extends T> type) throws FactoryException {
        if(instanceFactoryMap.containsKey(type)){
            return instanceFactoryMap.get(type).get(type);
        }

        return (T)throwUnsupportedTypeException(type);
    }
}
