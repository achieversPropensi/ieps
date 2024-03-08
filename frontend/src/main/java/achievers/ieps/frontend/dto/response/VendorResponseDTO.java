package achievers.ieps.frontend.dto.response;

import achievers.ieps.frontend.dto.request.CreateVendorRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorResponseDTO extends CreateVendorRequestDTO {
    private UUID id;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private String status;
}
