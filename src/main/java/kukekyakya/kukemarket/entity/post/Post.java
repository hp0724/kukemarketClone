package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import kukekyakya.kukemarket.entity.category.Category;
import kukekyakya.kukemarket.entity.common.EntityDate;
import kukekyakya.kukemarket.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    public ImageUpdatedResult update(PostUpdateRequest req){
        this.title=req.getTitle();
        this.content=req.getContent();
        this.price=req.getPrice();
        ImageUpdatedResult result =findImageUpdatedResult(req.getAddedImages(),req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        return result;
    }
    //인스턴스 변수 images에 Image를 추가하고, 해당 Image에 this(Post)를 등록해줍니다
    private void addImages(List<Image> added){ //5
        added.stream().forEach(i->{
            images.add(i);
            i.initPost(this);
        });
    }


    private void deleteImages(List<Image> deleted){
        deleted.stream().forEach(di->this.images.remove(di));
    }



    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles,List<Long> deletedImageIds){
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles,addedImages,deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
                .map(id->convertImageIdImage(id))
                .filter(i->i.isPresent())
                .map(i->i.get())
                .collect(toList());
    }
    private Optional<Image> convertImageIdImage(Long id){
        return this.images.stream().filter(i->i.getId().equals(id)).findAny();
    }
    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream().map(imageFile->new Image(imageFile.getOriginalFilename())).collect(toList());
    }

    // 이 정보를 가지고 실제 저장소에 추가될 이미지는 업로드하고 삭제될 이미지는 제거
    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult{
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;

    }


}
