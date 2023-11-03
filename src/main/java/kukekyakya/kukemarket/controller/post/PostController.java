package kukekyakya.kukemarket.controller.post;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kukekyakya.kukemarket.aop.AssignMemberId;
import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "Post Controller",tags="Post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    // Content-Type이 multipart/form-data를 이용해야합니다.
    //따라서 PostCreateRequest 파라미터에 @ModelAttribute를 선언해줍니다.
    @ApiOperation(value = "게시글 생성 ",notes = "게시글을 생성한다.")
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create (@Valid @ModelAttribute PostCreateRequest req) {
        return Response.success(postService.create(req));
    }
}
