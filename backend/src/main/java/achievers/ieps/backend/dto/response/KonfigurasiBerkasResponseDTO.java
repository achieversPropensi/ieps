package achievers.ieps.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KonfigurasiBerkasResponseDTO {
    private String namaBerkas;
    private boolean isDeleted;
    private String berkasId;
    private String deskripsi;
}
