package kukekyakya.kukemarket.entity.comment;

import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Comment  extends EntityDate {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    //삭제 여부 표시
    @Column(nullable = false)
    private boolean deleted;

    //@OnDelete(action=onDeleteAction.CASCADE)를 설정해놨기 때문에,
    // Member나 Category,상위 Comment가 제거된다면,
    // 연쇄적으로 현재의 댓글도 제거될 것입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parent;

    //각 댓글의 하위 댓글들을 참조할 수 있도록, @OneToMany 관계를 맺어줍니다.
    @OneToMany(mappedBy = "parent")
    private List<Comment> children=new ArrayList<>();

    public Comment (String content,Member member,Post post,Comment parent){
        this.content=content;
        this.member=member;
        this.post =post;
        this.parent=parent;
        this.deleted =false;
    }

    public Optional<Comment> findDeletableComment() { // 6
        return hasChildren() ? Optional.empty() : Optional.of(findDeletableCommentByParent());
    }

    public void delete() { // 7
        this.deleted = true;
    }
    //상위 댓글로 거슬러올라가면서, 실제로 제거해도 되는 댓글을 찾아낼 것입니다.
    private Comment findDeletableCommentByParent() { // 1
        if (isDeletedParent()) {
            Comment deletableParent = getParent().findDeletableCommentByParent();
            if(getParent().getChildren().size() == 1) return deletableParent;
        }
        return this;
    }
    //하위 댓글이 있는지 판별합니다.
    private boolean hasChildren() { // 9
        return getChildren().size() != 0;
    }
    // 현재 댓글의 상위 댓글이 제거해도 되는 것인지 판별합니다.
    private boolean isDeletedParent() { // 2
        return getParent() != null && getParent().isDeleted();
    }

}
