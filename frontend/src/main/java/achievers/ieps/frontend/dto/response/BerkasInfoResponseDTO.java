package achievers.ieps.frontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BerkasInfoResponseDTO {
    private Long id;
    private String nama;
    private String deskripsi;
    private String judul;
    private String url;
    private String type;
}
