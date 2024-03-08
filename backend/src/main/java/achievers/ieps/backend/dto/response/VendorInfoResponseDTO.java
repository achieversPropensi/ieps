package achievers.ieps.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorInfoResponseDTO {
    private UUID id;
    private String status;
    private boolean hasSubmitted;
}
