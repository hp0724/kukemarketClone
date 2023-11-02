package kukekyakya.kukemarket.entity.post;

import kukekyakya.kukemarket.exception.UnsupportedImageFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false )
    private String originName;

    @ManyToOne (fetch = FetchType.LAZY)
    //외래키 느낌?
    @JoinColumn(name = "post_id",nullable = false)
    //게시글이 제거되면 이미지도 연쇄적으로 제거
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post ; //1

    //2 해당 이미지가 지원하는 이미지 확장자
    private final static String supportedExtension[] = {"jpg", "jpeg", "gif", "bmp", "png"};

    public Image(String originName){
        //고유 이름 부여
        this.uniqueName = generateUniqueName(extractExtension(originName)); // 3
        this.originName = originName;
    }

    //Post의 연관 관계에 대한 정보가 없다면 이를 등록
    public void initPost(Post post){ //4
        if(this.post==null){
            this.post=post;
        }
    }
    // 고유한 이름을 생성하기 위한 메소드입니다. 여기에서는 단순하게 UUID를 이용
    private String generateUniqueName(String extension){ //5
        return UUID.randomUUID().toString()+"."+extension;
    }

    //이미지 이름에서 확장자를 추
    private String extractExtension(String originName){ //6
        try {
            String ext = originName.substring(originName.lastIndexOf(".")+1);
            if(isSupportedFormat(ext)) return ext ;

        }catch (StringIndexOutOfBoundsException e) {

        }
        throw  new UnsupportedImageFormatException();
    }

    //확장자 검사
    private boolean isSupportedFormat(String ext){ //7
        return Arrays.stream(supportedExtension).anyMatch(e->e.equalsIgnoreCase(ext));
    }
}
