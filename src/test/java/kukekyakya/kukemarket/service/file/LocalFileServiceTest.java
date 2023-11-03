package kukekyakya.kukemarket.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalFileServiceTest {
    LocalFileService localFileService = new LocalFileService() ;
    //테스트 전용 location
    String testLocation = new File("src/test/resources/static").getAbsolutePath()+"/";

    //모든 파일 제거
    @BeforeEach
    void beforeEach () throws IOException {
        ReflectionTestUtils.setField(localFileService,"location",testLocation);
        FileUtils.cleanDirectory(new File(testLocation));
    }
    @Test
    void uploadTest(){
        //파일 명,미디어타입,바이트 배열
        MultipartFile file = new MockMultipartFile("myFile", "myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "testFile.txt";

        localFileService.upload(file,filename);
        assertThat(isExists(testLocation+filename)).isTrue();
    }

    boolean isExists(String filePath){
        return new File(filePath).exists();
    }
}
