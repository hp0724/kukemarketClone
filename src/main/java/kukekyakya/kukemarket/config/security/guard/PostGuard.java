package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.AccessDeniedException;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard {
    private final AuthHelper authHelper;
    private final PostRepository postRepository;

    public boolean check(Long id) {
        return authHelper.isAuthenticated() && hasAuthority(id);
    }

    //요청자가 관리자 이거나 게시글의 작성자라면 요청을 수행할수 있다.
    public boolean hasAuthority(Long id){
        //관리자 권한검사가 자원소유자 검사보다 먼저 이뤄져야 한다. !!!!
        // A가 참이면 B는 수행 하지 않으니 데이터베이스 접근을 두번안해도 되니깐 비용 최소화 할수 있다.
        return hasAdminRole() || isResourceOwner(id);
    }

    private boolean hasAdminRole(){
        return authHelper.extractMemberRoles().contains(RoleType.ROLE_ADMIN);
    }
    private boolean isResourceOwner(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->{throw new AccessDeniedException();});
        Long memberId = authHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }
}
