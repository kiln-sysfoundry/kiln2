package org.sysfoundry.kiln.factory;

import com.google.common.collect.ImmutableMap;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class Util {

    public static <K,V> Map<K,V> imMap(AbstractMap.SimpleEntry<K,V>... entries){
        return new ImmutableMap.Builder<K,V>().putAll(Arrays.asList(entries)).build();
    }

    public static <K,V> AbstractMap.SimpleEntry<K,V> entry(K key,V val){
        return new AbstractMap.SimpleEntry<>(key,val);
    }

    public static AbstractMap.SimpleEntry<Class, InstanceFactory> singleton(InstanceFactory parent,
                                                                            Class type,
                                                                            ConstructorFn creator){
        return new AbstractMap.SimpleEntry<Class,InstanceFactory>(type,new SingletonInstanceFactory(parent,type,creator));
    }

    public static AbstractMap.SimpleEntry<Class, InstanceFactory> prototype(InstanceFactory parent,
                                                                            Class type,
                                                                            ConstructorFn creator){
        return new AbstractMap.SimpleEntry<Class,InstanceFactory>(type,new PrototypeInstanceFactory(parent,type,creator));
    }

    public static AbstractMap.SimpleEntry<Class, InstanceFactory> obj(InstanceFactory parent,
                                                                            Class type,
                                                                            Object instance){
        return new AbstractMap.SimpleEntry<Class,InstanceFactory>(type,new ObjectInstanceFactory(parent,type,instance));
    }


}
