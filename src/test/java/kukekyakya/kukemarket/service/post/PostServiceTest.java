package kukekyakya.kukemarket.service.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.post.PostDto;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.exception.MemberNotFoundException;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.repository.post.PostRepositoryTest;
import kukekyakya.kukemarket.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImage;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @Test
    void createTest(){
        PostCreateRequest req = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0,req.getImages().size()).mapToObj(i->createImage()).collect(Collectors.toList())
        ));

        postService.create(req);

        verify(postRepository).save(any());
        verify(fileService,times(req.getImages().size())).upload(any(),anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest(){
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(()->postService.create(createPostCreateRequest())).isInstanceOf(MemberNotFoundException.class);

    }

    @Test
    void createExceptionByCategoryNotFoundTest(){
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(()->postService.create(createPostCreateRequest())).isInstanceOf(CategoryNotFoundException.class);

    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest(){
        PostCreateRequest req= createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test","test.txt", MediaType.TEXT_PLAIN_VALUE,"test".getBytes()))
        );

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        assertThatThrownBy(()-> postService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);

    }

    @Test
    void readTest(){
        Post post = createPostWithImages(List.of(createImage(),createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        PostDto postDto = postService.read(1L);

        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
    }

    @Test
    void readExceptionByPostNotFoundTest(){
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(()->postService.read(1L)).isInstanceOf(PostNotFoundException.class);
    }


    @Test
    void deleteTest(){
        Post post = createPostWithImages(List.of(createImage(),createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        postService.delete(1L);
        // delete 메서드가 Post 객체와 연관된 이미지의 수와 동일한 횟수로 호출되는지 확인합니다.
        // anyString()을 사용하여 이 메서드가 호출된 횟수가 중요하며 삭제되는 특정 문자열은 중요하지 않음을 나타냅니다.
        verify(fileService,times(post.getImages().size())).delete(anyString());
        // any() 인자 매처는 delete 메서드가 어떤 인수로 호출되었는지는 중요하지 않고 메서드 호출 자체만을 확인합니다.
        verify(postRepository).delete(any());
    }
    @Test
    void deleteExceptionByNotFoundPostTest(){
        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(()->postService.delete(1L)).isInstanceOf(PostNotFoundException.class);
    }
}
