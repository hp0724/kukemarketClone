package kukekyakya.kukemarket.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    // 사용자를 인증하는데 필요한 최소한의 정보를 기억
    // 토큰의 타입과 CustomUserDetails , 권한 등급 정보
//    private String type;
    private CustomUserDetails principal;

    public CustomAuthenticationToken( CustomUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
//        this.type = type;
        this.principal = principal;
        setAuthenticated(true);
    }
    @Override
    public CustomUserDetails getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }

//    public String getType() {
//        return type;
//    }

}
