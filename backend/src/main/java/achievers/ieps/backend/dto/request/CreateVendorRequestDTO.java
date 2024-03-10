package achievers.ieps.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateVendorRequestDTO extends CreateUserRequestDTO {
    private String alamat;
    private String namaPerusahaan;
}
