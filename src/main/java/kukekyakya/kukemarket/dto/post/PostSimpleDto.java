package kukekyakya.kukemarket.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//단순히 게시글 id, 제목, 작성자 닉네임, 작성일자만 가지고 있겠습니다.
public class PostSimpleDto {
    private Long id;
    private String title;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
