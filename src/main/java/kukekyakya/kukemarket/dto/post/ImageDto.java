package kukekyakya.kukemarket.dto.post;

import kukekyakya.kukemarket.entity.post.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;

    //원래의 파일 명과 서버에서 생성한 고유한 파일명으로 DTO 를 생성해서 반환
    public static ImageDto toDto(Image image){
        return new ImageDto(image.getId(),image.getOriginName(),image.getUniqueName());
    }
}
