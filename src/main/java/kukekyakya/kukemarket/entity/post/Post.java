package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private Long price;


    @ManyToOne(fetch = FetchType.LAZY)
    //member 가 없으면 포스트가 없으니깐 nullalbe =false
    @JoinColumn(name = "member_id",nullable = false)
    //member 지우면 다없어짐
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member; //1

    @ManyToOne(fetch = FetchType.LAZY)
    //카테고리 가 없으면 포스트가 없으니깐 nullalbe =false
    @JoinColumn(name = "category_id",nullable = false)
    //카테고리 지우면 다없어짐
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category; //2

    //게시글이 처음 저장될때 게시글에 등록했던 이미지도 함께 저장
    // 게시글이 삭제되면 모든 이미지 데이터도 삭제
    @OneToMany(mappedBy = "post",cascade = CascadeType.PERSIST,orphanRemoval = true)
    private List<Image> images; //3

    public Post(String title, String content, Long price, Member member, Category category, List<Image> images){
        this.title = title;
        this.content = content;
        this.price = price;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images); // 4
    }
    //인스턴스 변수 images에 Image를 추가하고, 해당 Image에 this(Post)를 등록해줍니다
    private void addImages(List<Image> added){ //5
        added.stream().forEach(i->{
            images.add(i);
            i.initPost(this);
        });
    }


}
