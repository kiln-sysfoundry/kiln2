package org.sysfoundry.kiln.types;

import lombok.NonNull;
import lombok.Value;

import java.util.Collection;
import java.util.Map;

@Value
public class TypedContainer {

    private Object wrappedInstance;
    private Class annotationType;
    private Class mainType;
    private Class valType;
    private Class keyType;
    private boolean isCollection;
    private boolean isMap;

    public TypedContainer(@NonNull Object inst,@NonNull Class annotationType){
        this(inst,annotationType,inst.getClass(),null,null);
    }

    public TypedContainer(@NonNull Object inst,@NonNull Class annotationType,@NonNull Class mtype){
        this(inst,annotationType,mtype,null,null);
    }

    public TypedContainer(@NonNull Object inst,@NonNull Class annotationType,@NonNull Class mtype,Class valtyp){
        this(inst,annotationType,mtype,valtyp,null);
    }

    public TypedContainer(@NonNull Object inst,@NonNull Class annotationType,@NonNull Class mtype,Class valtyp,Class keytyp){
        this.wrappedInstance = inst;
        this.annotationType = annotationType;
        this.mainType = mtype;
        this.valType = valtyp;
        this.keyType = keytyp;
        isCollection = Collection.class.isAssignableFrom(mtype);
        isMap = Map.class.isAssignableFrom(mtype);

        if(isCollection){
            if(valType == null){
                throw new RuntimeException(String.format("Valtype cannot be null for a Collection %s",inst));
            }
        }else if(isMap){
            if(valType == null || keyType == null){
                throw new RuntimeException(String.format("KeyType and ValType is mandatory for the Map %s",inst));
            }
        }
    }


    public boolean isEmpty(){
        return wrappedInstance == null;
    }

    public boolean isCollection(){
        return Collection.class.isAssignableFrom(mainType);
    }

    public boolean isMap(){
        return Map.class.isAssignableFrom(mainType);
    }

    public boolean isCompatibleWithType(@NonNull Class someType){
        return someType.isAssignableFrom(mainType);
    }


    public <T> T unwrapAs(Class<? extends T> expectedType) throws UnwrapFailedException {
        if(expectedType.isAssignableFrom(mainType)){
            return (T)wrappedInstance;
        }else{
            throw new UnwrapFailedException(String.format("Maintype %s does not confirm to expected type %s",mainType,expectedType));
        }
    }
}
