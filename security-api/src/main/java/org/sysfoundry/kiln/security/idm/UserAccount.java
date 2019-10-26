package org.sysfoundry.kiln.security.idm;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

public interface UserAccount {

    public Principal getPrincipal();

    public Set<String> getRoles();

    public Set<String> getScopes();

    public boolean isEmailVerified();

    public String getName();

    public String getPreferredName();

    public String getGivenName();

    public Map<String,Object> getOtherAttributes();

}
