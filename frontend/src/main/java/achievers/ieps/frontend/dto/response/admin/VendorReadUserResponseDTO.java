package achievers.ieps.frontend.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorReadUserResponseDTO {
    private UUID id;
    private String nama;
    private String email;
    private String role;
    private String nomorTelefon;
    private String password;

    private String alamat;
    private String namaPerusahaan;
    private String status;
}
