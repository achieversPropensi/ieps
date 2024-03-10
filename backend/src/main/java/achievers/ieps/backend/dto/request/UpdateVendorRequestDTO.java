package achievers.ieps.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVendorRequestDTO {
    private UUID id = UUID.randomUUID();
    private String nama;
    private String email;
    private String role;
    private String nomorTelefon;
//    private String password;

    private String alamat;
    private String namaPerusahaan;
    private String status;
}