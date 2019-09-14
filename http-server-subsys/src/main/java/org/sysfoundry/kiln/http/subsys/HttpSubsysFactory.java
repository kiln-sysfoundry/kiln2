package org.sysfoundry.kiln.http.subsys;

import org.sysfoundry.kiln.factory.ConstructionFailedException;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.http.HttpHandlerRoute;
import org.sysfoundry.kiln.http.HttpHandlerRouteFilter;
import org.sysfoundry.kiln.http.HttpServer;
import org.sysfoundry.kiln.http.HttpSubsysConfig;
import org.sysfoundry.kiln.types.TypedContainer;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class HttpSubsysFactory extends MapBaseInstanceFactory {
    public HttpSubsysFactory(InstanceFactory parent) {

        super(parent, imMap(
            singleton(parent, HttpServer.class, instanceFactory -> {
                try {
                    HttpSubsysConfig httpSubsysConfig = instanceFactory.get(HttpSubsysConfig.class);
                    HttpHandlerRouteFilter httpHandlerRouteFilter = instanceFactory.get(HttpHandlerRouteFilter.class);
                    Optional<Set<Set<HttpHandlerRoute>>> allRoutesOptonal = httpHandlerRouteFilter.getAll(Set.class);
                    Set<HttpHandlerRoute> allRoutesSet = new LinkedHashSet<>();
                    if(allRoutesOptonal.isPresent()){
                        for (Set<HttpHandlerRoute> httpHandlerRoutes : allRoutesOptonal.get()) {
                            allRoutesSet.addAll(httpHandlerRoutes);
                        }
                    }
                    return new HttpServerImpl(httpSubsysConfig.getServer(),allRoutesSet);
                } catch (FactoryException e) {
                    throw new ConstructionFailedException(e);
                }
            }),
                singleton(parent, HttpHandlerRouteFilter.class,instanceFactory -> {
                    return new HttpHandlerRouteFilter(instanceFactory);
                }),
                singleton(parent, TypedContainer.class,instanceFactory -> {
                    Set<HttpHandlerRoute> defaultRoutes = new LinkedHashSet<>();
                    defaultRoutes.add(new HttpHandlerRoute("get","/test/*",null,httpServerExchange -> {
                        httpServerExchange.getResponseSender().send("Test Message");
                    }));
                    defaultRoutes.add(new HttpHandlerRoute("post","/test/posttest/*",null,httpServerExchange -> {
                        httpServerExchange.getResponseSender().send("Test Post message");
                    }));
                    return new TypedContainer(defaultRoutes,HttpHandlerRoute.class,Set.class,HttpHandlerRoute.class);
                })

        ));

    }
}
