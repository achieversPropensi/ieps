package achievers.ieps.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    private String nama;

    @Email(message = "Email address tidak valid")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password minimal harus 8 karakter dengan kombinasi huruf besar, huruf kecil, dan angka")
    private String password;

    @Pattern(regexp = "(\\+?\\d+)",
            message = "Nomor telepon tidak valid")
    private String nomorTelefon;

    private String role;
}
