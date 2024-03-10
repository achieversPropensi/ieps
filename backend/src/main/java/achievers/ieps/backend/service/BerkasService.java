package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.backend.dto.request.UpdateBerkasRequestDTO;
import achievers.ieps.backend.model.Berkas;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BerkasService {
    Berkas store(CreateBerkasRequestDTO createBerkasRequestDTO) throws IOException;
    Berkas getBerkas(UUID vendorId, Long berkasId);
    List<Berkas> getAllBerkas(String id);
    void deleteBerkasVendor(String token);
    Berkas edit(UpdateBerkasRequestDTO updateBerkasRequestDTO) throws IOException;
}
