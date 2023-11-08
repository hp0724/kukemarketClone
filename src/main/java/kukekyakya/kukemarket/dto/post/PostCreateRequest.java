package kukekyakya.kukemarket.dto.post;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import kukekyakya.kukemarket.entity.post.Image;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApiOperation(value="게시글 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true, example = "my title")
    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 본문", notes = "게시글 본문을 입력해주세요", required = true, example = "my content")
    @NotBlank(message = "게시글 본문을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "가격", notes = "가격을 입력해주세요", required = true, example = "50000")
    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "0원 이상을 입력해주세요")
    private Long price;

    //문서에 나타나지 않도록 hidden
    @ApiModelProperty(hidden = true)
    @Null
    //클라이언트에게 전달받지 않기위해서 null 처리
    private Long memberId;

    @ApiModelProperty(value = "카테고리 아이디", notes = "카테고리 아이디를 입력해주세요", required = true, example = "3")
    @NotNull (message = "카테고리 아이디를 입력해주세요")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요")
    private Long categoryId;

    //Multipart 로 게시글에 대한 정보와 이미지 목록
    @ApiModelProperty(value = "이미지",notes = "이미지를 첨부해주세요.")
    private List<MultipartFile> images = new ArrayList<>();

    //이미지가 없을 경우의 NullPointerException을 대비하여 images는 비어있는 리스트로 초기화해주었습니
    public static Post toEntity(PostCreateRequest req, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        return new Post(
                //포스트 내용
                req.title,
                req.content,
                req.price,
                //작성자
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new),
                req.images.stream().map(i -> new Image(i.getOriginalFilename())).collect(toList())
        );

    }
}
