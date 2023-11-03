package kukekyakya.kukemarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.image.location}")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // url에 /image/ 접두 경로가 설정되어있으면
        registry.addResourceHandler("/image/**")
                // location 경로에서 파일에 접근
                .addResourceLocations("file:"+location)
                //업로드된 각각의 이미지는 고유한 이름을 가지고 있으며 수정되지 않을 것이기 때문에, 캐시를 설정
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());

    }
}
