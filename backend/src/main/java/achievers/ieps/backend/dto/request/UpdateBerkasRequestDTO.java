package achievers.ieps.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBerkasRequestDTO extends CreateBerkasRequestDTO {
    private Long id;
}
