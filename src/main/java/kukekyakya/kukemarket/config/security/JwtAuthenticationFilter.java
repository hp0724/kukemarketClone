package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.config.token.TokenHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {
    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailService userDetailService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if(validateAccessToken(token)){
            setAccessAuthentication( token);
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
        return token !=null && accessTokenHelper.validate(token);
    }

//    private boolean validateRefreshToken(String token){
//        return token !=null && tokenService.validateRefreshToken(token);
//
//    }

    private void setAccessAuthentication(String token){
        String userId = accessTokenHelper.extractSubject(token);
        CustomUserDetails userDetails = userDetailService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails,userDetails.getAuthorities()));
    }

//    private void setRefreshAuthentication(String type,String token){
//        String userId = tokenService.extractRefreshTokenSubject(token);
//        CustomUserDetails userDetails=userDetailService.loadUserByUsername(userId);
//        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(type,userDetails,userDetails.getAuthorities()));
//    }
}
