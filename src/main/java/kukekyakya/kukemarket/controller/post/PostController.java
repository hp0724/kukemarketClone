package kukekyakya.kukemarket.controller.post;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kukekyakya.kukemarket.aop.AssignMemberId;
import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.post.PostReadCondition;
import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import kukekyakya.kukemarket.dto.response.Response;
import kukekyakya.kukemarket.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value="게시글 조회",notes = "게시글을 조회한다.")
    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value="게시글 id",required=true) @PathVariable Long id){
        return Response.success(postService.read(id));
    }

    @ApiOperation(value="게시글 삭제" ,notes = "게시글을 삭제한다.")
    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    //삭제에 성공한 200 상태코드
    public Response delete(@ApiParam(value="게시글 id",readOnly = true) @PathVariable Long id){
        postService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "게시글 수정",notes = "게시글을 수정한다.")
    @PutMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    // @ModelAttribute
    //사용자의 입력 데이터를 컨트롤러로 전달할 때, 그 데이터를 모델 객체로 변환하여 전달하는 데에 쓰입니다.
    public Response update(
            @ApiParam(value="게시글 id",required = true) @PathVariable Long id,
            @Valid @ModelAttribute PostUpdateRequest req){
        return Response.success(postService.update(id,req));
    }
    @ApiOperation(value = "게시글 목록 조회",notes = "게시글 목록을 조회한다.")
    @GetMapping("/api/posts")
    @ResponseStatus(HttpStatus.OK)
    public  Response readAll(@Valid PostReadCondition cond){
        return Response.success(postService.readAll(cond));
    }

}
