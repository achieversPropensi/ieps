package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBerkasRequestDTO {
    private String nama;
    private String judul;
    private String type;
    private byte[] data;
    private String token;
}
