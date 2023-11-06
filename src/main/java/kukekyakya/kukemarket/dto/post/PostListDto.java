package kukekyakya.kukemarket.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListDto {
    private Long totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<PostSimpleDto> postList;
    //총 게시글 개수, 총 페이지 수, 다음 페이지가 있는지, 실제 페이지 내역을 가지고 있습니다.
    public static PostListDto toDto(Page<PostSimpleDto> page){
        return new PostListDto(page.getTotalElements(),page.getTotalPages(), page.hasNext(), page.getContent());
    }
}
