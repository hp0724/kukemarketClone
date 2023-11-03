package kukekyakya.kukemarket.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    //파일 업로드와 삭제를 수행
    void upload(MultipartFile file, String filename);
    void delete(String filename);
}
