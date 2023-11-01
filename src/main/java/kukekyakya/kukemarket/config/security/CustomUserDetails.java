package kukekyakya.kukemarket.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final Set<GrantedAuthority> authorities;

    // 권한 등급 정보
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    // 유저 정보
    @Override
    public String getUsername() {
        return userId;
    }

    // 다른 내용은 실제 사용하는것이 아니기 때문에 예외 발생
    @Override
    public String getPassword() {
        throw  new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();

    }
}
