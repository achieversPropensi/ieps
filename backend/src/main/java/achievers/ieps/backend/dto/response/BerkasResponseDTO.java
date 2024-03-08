package achievers.ieps.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BerkasResponseDTO {
    private Long id;
    private String nama;
    private String judul;
    private String url;
    private String type;
}
