package achievers.ieps.backend.dto.request;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBerkasRequestDTO {
    private String nama;
    private String judul;
    private String type;
    private byte[] data;
    private String token;
}
