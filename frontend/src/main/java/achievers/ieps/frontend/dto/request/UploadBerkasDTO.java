package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadBerkasDTO {
    private String nama;
    private String deskripsi;
    private MultipartFile file;
}
