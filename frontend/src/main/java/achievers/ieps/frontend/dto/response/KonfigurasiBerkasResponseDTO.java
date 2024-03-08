package achievers.ieps.frontend.dto.response;

import java.util.UUID;

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

}
