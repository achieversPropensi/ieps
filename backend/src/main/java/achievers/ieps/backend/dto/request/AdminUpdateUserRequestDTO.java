package achievers.ieps.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequestDTO {

    private UUID id = UUID.randomUUID();

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is required")
    private String nama;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number is required")
    private String nomorTelefon;

    @NotNull(message = "Role cannot be null")
    @NotBlank(message = "Role is required")
    private String role;
}
