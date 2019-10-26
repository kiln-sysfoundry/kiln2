package org.sysfoundry.kiln.http.subsys;

import java.security.Principal;

class DefaultPrincipal implements Principal {

    private String name;

    DefaultPrincipal(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DefaultPrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}
