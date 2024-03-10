package achievers.ieps.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateKonfigurasiBerkasRequestDTO {
    private String namaBerkas;
    private String deskripsi;
}