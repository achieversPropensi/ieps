package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBerkasDTO extends UploadBerkasDTO {
    private Long id;
}
