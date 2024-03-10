package achievers.ieps.frontend.dto.response;

import java.util.List;

import achievers.ieps.frontend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KonfigurasiBerkasWrapperRespondeDTO {
    private List<CreateKonfigurasiBerkasRequestDTO> listKB;

}

