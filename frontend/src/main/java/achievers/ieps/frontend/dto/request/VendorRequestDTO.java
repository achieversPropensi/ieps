package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorRequestDTO {
    private UUID id;
    private String nama;
    private String email;
    private String role;
    private String nomorTelefon;
//    private String password;

    private String alamat;
    private String namaPerusahaan;
    private String status;
}
