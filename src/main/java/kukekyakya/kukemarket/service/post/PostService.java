package kukekyakya.kukemarket.service.post;

import kukekyakya.kukemarket.dto.post.PostCreateRequest;
import kukekyakya.kukemarket.dto.post.PostCreateResponse;
import kukekyakya.kukemarket.dto.post.PostDto;
import kukekyakya.kukemarket.entity.post.Image;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.exception.PostNotFoundException;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import kukekyakya.kukemarket.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public PostCreateResponse create(PostCreateRequest req){
        //전달받은 PostCreateRequest를 엔티티로 변환하고
        Post post =postRepository.save(
                PostCreateRequest.toEntity(
                        req,
                        memberRepository,
                        categoryRepository
                )
        );
        //이미지가 있다면 FileServlice.upload를 통해 이미지 업로드
        uploadImages(post.getImages(),req.getImages());
        return new PostCreateResponse(post.getId());
    }
    private void uploadImages(List<Image> images , List<MultipartFile> fileImages){
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i),images.get(i).getUniqueName()));
    }

    public PostDto read(Long id) {
        return PostDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @Transactional
    public void delete(Long id ){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        deleteImages(post.getImages());
        postRepository.delete(post);
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i ->fileService.delete(i.getUniqueName()));
    }
}
