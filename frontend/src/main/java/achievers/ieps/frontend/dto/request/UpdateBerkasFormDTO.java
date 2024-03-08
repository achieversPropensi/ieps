package achievers.ieps.frontend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBerkasFormDTO {
    private List<UpdateBerkasDTO> listBerkas;
}
