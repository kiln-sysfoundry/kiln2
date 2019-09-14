package org.sysfoundry.kiln.types;

import lombok.NonNull;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.UnsupportedInstanceTypeException;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseTypedContainerFilter<T> {

    protected InstanceFactory instanceFactory;
    protected Class mainType;
    protected Class annotationType;
    protected Optional<Set<T>> filteredResults;

    public BaseTypedContainerFilter(@NonNull InstanceFactory factory,@NonNull Class annotationType
            ,@NonNull Class<? extends T> mainType){
        this.instanceFactory = factory;
        this.mainType = mainType;
        this.annotationType = annotationType;
    }

    public InstanceFactory getInstanceFactory() {
        return instanceFactory;
    }

    public Class getMainType() {
        return mainType;
    }

    public Class getAnnotationType() {
        return annotationType;
    }

    public Optional<Set<T>> getAll(Class<? extends T> mtype) throws FilteringFailedException {
        if(mtype.isAssignableFrom(mainType)){
            if(filteredResults == null){
                synchronized (mainType){
                    if(filteredResults == null){
                        Set<TypedContainer> typedContainerSet = new LinkedHashSet<>();
                        try {
                                typedContainerSet = instanceFactory.getAll(TypedContainer.class);
                        } catch (UnsupportedInstanceTypeException e) {
                            //e.printStackTrace();

                        }catch(FactoryException e){
                            throw new FilteringFailedException(e);
                        }

                            Set<TypedContainer> matchingTypedContainerSet = typedContainerSet.stream().filter(typedContainer -> {
                                return ((
                                            annotationType.isAssignableFrom(typedContainer.getAnnotationType())
                                        ) && (
                                            mainType.isAssignableFrom(typedContainer.getMainType())
                                        ));
                            }).collect(Collectors.toSet());

                            Set<T> allItems = matchingTypedContainerSet.stream().map(typedContainer -> {
                                try {
                                    return typedContainer.unwrapAs(mtype);
                                } catch (UnwrapFailedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).collect(Collectors.toSet());

                            filteredResults = Optional.ofNullable(allItems);
                    }
                }
            }
            return filteredResults;
        }else{
            throw new FilteringFailedException(String.format("Maintype %s does not confirm to expected type %s",mainType,mtype));
        }
    }

    public Optional<T> get(Class<? extends T> mtype) throws FilteringFailedException {
        Optional<T> optionalRetVal = Optional.empty();
        Optional<Set<T>> allMatchingVals = null;
        allMatchingVals = getAll(mtype);
        if(allMatchingVals.isPresent()){
            Set<T> allVals = allMatchingVals.get();
            if(!allVals.isEmpty()){
                Object[] objects = allVals.toArray(new Object[0]);
                optionalRetVal = Optional.ofNullable((T)objects[0]);
            }
        }
        return optionalRetVal;
    }
}
