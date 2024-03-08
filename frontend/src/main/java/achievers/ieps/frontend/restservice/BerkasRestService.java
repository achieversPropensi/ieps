package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.frontend.dto.request.UploadBerkasDTO;
import achievers.ieps.frontend.dto.response.BerkasInfoResponseDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BerkasRestService {
    List<KonfigurasiBerkasResponseDTO> retrieveKonfigurasi(String token);
    List<BerkasInfoResponseDTO> retrieveAllBerkas(String token);
    Map<String, String> uploadBerkas(String token, CreateBerkasRequestDTO uploadBerkasDTO);
    Map<String, String> updateBerkas(String token, CreateBerkasRequestDTO uploadBerkasDTO);
}
