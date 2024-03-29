package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.config.security.CustomAuthenticationToken;
import kukekyakya.kukemarket.config.security.CustomUserDetails;
import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthHelper {
      public static boolean isAuthenticated(){
          return getAuthentication() instanceof CustomAuthenticationToken &&
                  getAuthentication().isAuthenticated();
      }

      public static Long extractMemberId(){

          return Long.valueOf(getUserDetails().getUserId());
      }

      public static Set<RoleType> extractMemberRoles(){
         return getUserDetails().getAuthorities()
                 .stream()
                 .map(authority -> authority.getAuthority())
                 .map(strAuth ->RoleType.valueOf(strAuth))
                 .collect(Collectors.toSet());
     }



     private static CustomUserDetails getUserDetails(){
         return (CustomUserDetails) getAuthentication().getPrincipal();

     }
     private static Authentication getAuthentication(){
         return SecurityContextHolder.getContext().getAuthentication();
     }


}
