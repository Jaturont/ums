package org.example.usermanagement.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null); // No authorities granted yet
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false); // By default, the token is not authenticated
    }

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities); // Pass the authorities when the token is authenticated
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true); // Set the token as authenticated
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}