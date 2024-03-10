package achievers.ieps.frontend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorInfoResponseDTO {
    private UUID id;
    private String status;
    private boolean hasSubmitted;
}
