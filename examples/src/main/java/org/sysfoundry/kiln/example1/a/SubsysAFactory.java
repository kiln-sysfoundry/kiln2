package org.sysfoundry.kiln.example1.a;

import com.google.common.collect.ImmutableMap;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.factory.SingletonInstanceFactory;

public class SubsysAFactory extends MapBaseInstanceFactory {

    public SubsysAFactory(InstanceFactory parent) {
        super(parent,new ImmutableMap.Builder<Class,InstanceFactory>()
                .put(A.class,new SingletonInstanceFactory<A>(parent,A.class,instanceFactory->{
                    return new A() {
                    };
                }))
                .put(X.class,new SingletonInstanceFactory<X>(parent,X.class,instanceFactory -> {
                    return new X(){

                    };
                }))
                .build());
    }

}
