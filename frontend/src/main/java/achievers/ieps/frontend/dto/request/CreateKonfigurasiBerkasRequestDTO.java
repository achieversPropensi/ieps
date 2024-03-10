package achievers.ieps.frontend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateKonfigurasiBerkasRequestDTO {
    
    @NotBlank(message = "Nama berkas tidak boleh kosong")
    @NotNull(message = "Nama berkas tidak boleh kosong")
    @Size(max = 200, message = "Nama berkas tidak boleh lebih dari 250 karakter")
    private String namaBerkas;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @NotNull(message = "Deskripsi tidak boleh kosong")
    @Size(max = 200, message = "Deskripsi tidak boleh lebih dari 250 karakter")
    private String deskripsi;
}