package org.sysfoundry.kiln.factory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class Util {

    public static <K,V> Map<K,V> imMap(AbstractMap.SimpleEntry<K,V>... entries){
        return new ImmutableMap.Builder<K,V>().putAll(Arrays.asList(entries)).build();
    }

    public static <K,V> Multimap<K,V> imMultimap(AbstractMap.SimpleEntry<K,V>... entries){
        return new ImmutableMultimap.Builder<K,V>().putAll(Arrays.asList(entries)).build();
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


    public static <T> Set<T> flattenSets(Optional<Set<Set<T>>> optionalSets,Class<? extends T> typ){
        Set<T> flattenedSet = new LinkedHashSet<>();
        if(optionalSets.isPresent()){
            for (Set<T> setOfObjs : optionalSets.get()) {
                flattenedSet.addAll(setOfObjs);
            }

        }
        return flattenedSet;
    }
}
