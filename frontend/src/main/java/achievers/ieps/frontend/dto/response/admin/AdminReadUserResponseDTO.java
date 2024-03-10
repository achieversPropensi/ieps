package achievers.ieps.frontend.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReadUserResponseDTO {
    private UUID id;
    private String nama;
    private String email;
    private String password;
    private String nomorTelefon;
    private String role;
}
