package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBerkasRequestDTO {
    private String nama;
    private String deskripsi;
    private String judul;
    private String type;
    private byte[] data;
    private String token;
}
