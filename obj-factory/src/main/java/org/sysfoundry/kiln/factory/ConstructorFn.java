package org.sysfoundry.kiln.factory;

@FunctionalInterface
public interface ConstructorFn<T> {

    public T construct(InstanceFactory instanceFactory) throws ConstructionFailedException;
}
