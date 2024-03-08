package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.backend.dto.request.UpdateBerkasRequestDTO;
import achievers.ieps.backend.model.Berkas;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface BerkasService {
    Berkas store(CreateBerkasRequestDTO createBerkasRequestDTO) throws IOException;
    Berkas getBerkas(UUID vendorId, Long berkasId);
    List<Berkas> getAllBerkas(String id);
    void deleteBerkasVendor(String token);
    Berkas edit(UpdateBerkasRequestDTO updateBerkasRequestDTO) throws IOException;
}
