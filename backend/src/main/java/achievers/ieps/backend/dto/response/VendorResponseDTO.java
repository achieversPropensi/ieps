package achievers.ieps.backend.dto.response;

import achievers.ieps.backend.dto.request.CreateVendorRequestDTO;
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
    private String role;
    private boolean isDeleted;
    private String status;
}