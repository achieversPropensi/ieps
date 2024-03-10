package achievers.ieps.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBerkasRequestDTO {
    private String nama;
    private String deskripsi;
    private String judul;
    private String type;
    private byte[] data;
    private String token;
}
