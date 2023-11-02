package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.Test;

import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImage;
import static kukekyakya.kukemarket.factory.entity.ImageFactory.createImageWithOriginName;
import static kukekyakya.kukemarket.factory.entity.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageTest {
    @Test
    void createImageTest(){
        String validExtension = "JPEG";
        createImageWithOriginName("image."+validExtension);
    }
    @Test
    void createImageExceptionByUnsupportedFormatTest(){
        String invalidExtension ="invalid";

        assertThatThrownBy(()->createImageWithOriginName("image"+invalidExtension))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void createImageExceptionByNoneExtensionTest() {
        // given
        String originName = "image";

        // when, then
        assertThatThrownBy(() -> createImageWithOriginName(originName))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void initPostTest(){
        Image image=createImage();

        Post post = createPost();
        image.initPost(post);

        assertThat(image.getPost()).isSameAs(post);
    }

    @Test
    void initPostNotChangeTest(){
        // given
        Image image = createImage();
        image.initPost(createPost());

        // when
        Post post = createPost();
        image.initPost(post);

        // then
        assertThat(image.getPost()).isNotSameAs(post);
    }



}
