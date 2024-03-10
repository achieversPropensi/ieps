package achievers.ieps.frontend.dto.response;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModelResponseDTO {
    @NotNull(message = "Tolong isi semua field data")
    private String id;
    @NotNull(message = "Tolong isi semua field data")
    private String nama;
    @NotNull(message = "Tolong isi semua field data")
    @Email(message = "Email address tidak valid")
    private String email;
    @NotNull(message = "Tolong isi semua field data")
    private String role;
    @NotNull(message = "Tolong isi semua field data")
    @Pattern(regexp = "(\\+?\\d+)",
            message = "Nomor telepon tidak valid")
    private String nomorTelefon;
    @NotNull(message = "Tolong isi semua field data")
    private String password;

    private String alamat;
    private String namaPerusahaan;
    private String status;
    private String emailToken;
}
