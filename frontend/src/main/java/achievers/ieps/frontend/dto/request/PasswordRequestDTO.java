package achievers.ieps.frontend.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequestDTO {
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password minimal harus 8 karakter dengan kombinasi huruf besar, huruf kecil, dan angka")
    private String passwordBaru;
    private String passwordBaruKonfirmasi;
}
