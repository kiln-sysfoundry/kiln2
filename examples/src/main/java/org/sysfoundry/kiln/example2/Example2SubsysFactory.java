package org.sysfoundry.kiln.example2;

import com.google.common.collect.ImmutableSet;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.http.HttpHandlerRoute;
import org.sysfoundry.kiln.types.TypedContainer;

import java.util.Set;

import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class Example2SubsysFactory extends MapBaseInstanceFactory {

    public Example2SubsysFactory(InstanceFactory parent) {
        super(parent,imMap(
                singleton(parent, TypedContainer.class,instanceFactory -> {
                    TestController testController = new TestController();
                    Set<HttpHandlerRoute> routes = new ImmutableSet.Builder<HttpHandlerRoute>()
                            .add(new HttpHandlerRoute("get","/accounts",null,testController.getAccounts()))
                            .build();
                    return new TypedContainer(routes,HttpHandlerRoute.class,Set.class,HttpHandlerRoute.class);
                })
        ));
    }
}
