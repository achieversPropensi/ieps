package achievers.ieps.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModelResponseDTO {
    private String id;
    private String nama;
    private String email;
    private String role;
    private String nomorTelefon;
    private String password;

    private String alamat;
    private String namaPerusahaan;
    private String status;
    private String emailToken;
}
