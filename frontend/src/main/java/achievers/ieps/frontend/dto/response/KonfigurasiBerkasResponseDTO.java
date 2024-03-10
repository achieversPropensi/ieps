package achievers.ieps.frontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KonfigurasiBerkasResponseDTO {
    private String berkasId;
    private String namaBerkas;
    private boolean isDeleted;
    private String deskripsi;
}
