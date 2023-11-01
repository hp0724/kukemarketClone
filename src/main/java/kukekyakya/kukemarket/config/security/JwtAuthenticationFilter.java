package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.service.sign.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {
    private final TokenService tokenService;
    private final CustomUserDetailService userDetailService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if(validateAccessToken(token)){
            setAccessAuthentication("access",token);
        }
//        else if (validateRefreshToken(token)){
//            setRefreshAuthentication("refresh",token);
//        }
        chain.doFilter(request,response);

    }

    private String extractToken(ServletRequest request){
        return ((HttpServletRequest)request).getHeader("Authorization");

    }
    private boolean validateAccessToken (String token){
        return token !=null && tokenService.validateAccessToken(token);
    }

//    private boolean validateRefreshToken(String token){
//        return token !=null && tokenService.validateRefreshToken(token);
//
//    }

    private void setAccessAuthentication(String type,String token){
        String userId = tokenService.extractAccessTokenSubject(token);
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails,userDetails.getAuthorities()));
    }

//    private void setRefreshAuthentication(String type,String token){
//        String userId = tokenService.extractRefreshTokenSubject(token);
//        CustomUserDetails userDetails=userDetailService.loadUserByUsername(userId);
//        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type,userDetails,userDetails.getAuthorities()));
//    }
}
