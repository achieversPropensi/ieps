package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModelRequestDTO {
    private String id;
    private String nama;
    private String email;
    private String role;
    private String nomorTelefon;
}
