package org.sysfoundry.kiln.factory;

import java.util.Set;

public interface InstanceFactory {

    public Set<Class> getProvisions();

    //public Set<Class> getRequirements();

    public <T> T get(Class<? extends T> type) throws FactoryException;

    public <T> Set<T> getAll(Class<? extends T> type) throws FactoryException;

}
