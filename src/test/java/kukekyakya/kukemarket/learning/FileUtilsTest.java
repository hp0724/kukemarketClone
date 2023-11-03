package kukekyakya.kukemarket.learning;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileUtilsTest {
    String testLocation = new File("src/test/resources/static").getAbsolutePath() + "/";

    //파일내 삭제
    @Test
    void cleanDirectoryTest() throws Exception{
        String filePath =testLocation+"cleanDirectoryTest.txt";
        MultipartFile file=new MockMultipartFile("myFile","myFile.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        file.transferTo(new File(filePath));
        boolean beforeCleaning= isExists(filePath);

        FileUtils.cleanDirectory(new File(testLocation));

        boolean afterCleaning = isExists(filePath);
        assertThat(beforeCleaning).isTrue();
        assertThat(afterCleaning).isFalse();
    }

    boolean isExists (String filePath){
        return new File(filePath).exists();
    }
}
