package kukekyakya.kukemarket.repository.comment;

import kukekyakya.kukemarket.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c left join fetch c.parent where c.id=:id")
    Optional<Comment> findWithParentById(Long id);
    //부모의 아이디로 오름차순 정렬하되 NULL을 우선적으로 하고, 그 다음으로 자신의 아이디로 오름차순 정렬하여 조회합니다.
    // 이 쿼리는 모든 댓글 목록을 조회할 때 사용될 것입니다.

    @Query("select c from Comment c join fetch c.member left join fetch c.parent where c.post.id = :postId order by c.parent.id asc nulls first, c.id asc")
    List<Comment> findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(Long postId);


}
