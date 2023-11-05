package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.dto.post.PostUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static kukekyakya.kukemarket.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static kukekyakya.kukemarket.factory.entity.CategoryFactory.createCategory;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImageWithIdAndOriginName;
import static kukekyakya.kukemarket.factory.entity.MemberFactory.createMember;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPostWithImages;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
    @Test
    void updateTest(){
        //기존에 a,b
        Image a =createImageWithIdAndOriginName(1L,"a.jpg");
        Image b =createImageWithIdAndOriginName(2L,"b.jpg");
        Post post = createPostWithImages(createMember(),createCategory(), List.of(a,b));

        MockMultipartFile cFile =new MockMultipartFile("c","c.png", MediaType.IMAGE_PNG_VALUE,"cFile".getBytes());
        //c 추가하고 a 제거하기
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("update tile","update content",12345L,List.of(cFile),List.of(a.getId()));
        Post.ImageUpdatedResult imageUpdatedResult =post.update(postUpdateRequest);

        assertThat(post.getTitle()).isEqualTo(postUpdateRequest.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateRequest.getContent());
        assertThat(post.getPrice()).isEqualTo(postUpdateRequest.getPrice());

        List<Image> resultImages = post.getImages();
        List<String> resultOriginNames =resultImages.stream().map(i->i.getOriginName()).collect(toList());
        assertThat(resultImages.size()).isEqualTo(2);
        assertThat(resultOriginNames).contains(b.getOriginName(),cFile.getOriginalFilename());

        List<MultipartFile> addedImageFiles = imageUpdatedResult.getAddedImageFiles();
        assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedImageFiles.get(0).getOriginalFilename()).isEqualTo(cFile.getOriginalFilename());

        List<Image> addedImages = imageUpdatedResult.getAddedImages();
        List<String> addedOriginNames = addedImages.stream().map(i->i.getOriginName()).collect(toList());
        assertThat(addedImageFiles.size()).isEqualTo(1);
        assertThat(addedOriginNames).contains(cFile.getOriginalFilename());

        List<Image> deletedImages = imageUpdatedResult.getAddedImages();
        List<String> deletedOriginNames = deletedImages.stream().map(i->i.getOriginName()).collect(toList());
        assertThat(deletedImages.size()).isEqualTo(1);
        assertThat(deletedOriginNames).contains(cFile.getOriginalFilename());



    }
}
