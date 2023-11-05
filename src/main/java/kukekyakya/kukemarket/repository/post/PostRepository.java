package kukekyakya.kukemarket.repository.post;

import kukekyakya.kukemarket.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
//게시글 조회할때 작성자의 정보도 같이 보내주기
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("select p from Post p join fetch p.member where p.id =:id")
    Optional<Post> findByIdWithMember(Long id);
}
