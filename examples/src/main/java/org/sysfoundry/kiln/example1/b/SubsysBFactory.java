package org.sysfoundry.kiln.example1.b;

import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;

import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class SubsysBFactory extends MapBaseInstanceFactory {

    public SubsysBFactory(InstanceFactory parent) {
        super(parent,imMap(
                singleton(parent,B.class,instanceFactory->{
                    return new B(){};
                })
        ));
    }
}
