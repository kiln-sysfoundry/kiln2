package org.sysfoundry.kiln.http.subsys;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.undertow.security.idm.Account;
import org.sysfoundry.kiln.security.idm.UserAccount;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultUserAccount implements UserAccount, Account {

    private String name;
    private String preferredName;
    private String givenName;
    private boolean emailVerified;
    private Set<String> scopes;
    private Set<String> roles;
    private Principal principal;
    private Map<String,Object> attributes;

    DefaultUserAccount(String name,String preferredName,String givenName,boolean emailVerified,
                       Set<String> scopes,Set<String> roles,Map<String,Object> attributes){
        Objects.nonNull(name);
        Objects.nonNull(scopes);
        Objects.nonNull(roles);
        Objects.nonNull(attributes);
        this.name = name;
        this.preferredName = preferredName;
        this.givenName = givenName;
        this.emailVerified = emailVerified;
        this.scopes = new ImmutableSet.Builder<String>().addAll(scopes.iterator()).build();
        this.roles = new ImmutableSet.Builder<String>().addAll(roles.iterator()).build();
        this.principal = new DefaultPrincipal(this.name);
        this.attributes = new ImmutableMap.Builder<String,Object>().putAll(attributes).build();

    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Set<String> getScopes() {
        return scopes;
    }

    @Override
    public boolean isEmailVerified() {
        return emailVerified;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPreferredName() {
        return preferredName;
    }

    @Override
    public String getGivenName() {
        return givenName;
    }

    @Override
    public Map<String, Object> getOtherAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "DefaultUserAccount{" +
                "name='" + name + '\'' +
                ", preferredName='" + preferredName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", emailVerified=" + emailVerified +
                ", scopes=" + scopes +
                ", roles=" + roles +
                ", principal=" + principal +
                ", attributes=" + attributes +
                '}';
    }
}
