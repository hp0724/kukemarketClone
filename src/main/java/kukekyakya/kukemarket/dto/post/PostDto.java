package kukekyakya.kukemarket.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.service.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
public class PostDto {
    private Long id ;
    private String title;
    private String content;
    private Long price;
    private MemberDto member;
    private List<ImageDto> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
    //작성자의 정보도 함께 가지고 있다 .
    //각 게시글이 가지고 있는 이미지에 대한 정보도 반환
    public static  PostDto toDto(Post post){
        return new PostDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getPrice(),
            MemberDto.toDto(post.getMember()),
            post.getImages().stream().map(image -> ImageDto.toDto(image)).collect(toList()),
            post.getCreatedAt(),
            post.getModifiedAt()
        );

    }
}
