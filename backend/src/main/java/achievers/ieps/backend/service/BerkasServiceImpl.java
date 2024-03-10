package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.backend.dto.request.UpdateBerkasRequestDTO;
import achievers.ieps.backend.model.Berkas;
import achievers.ieps.backend.model.Vendor;
import achievers.ieps.backend.repository.BerkasDb;
import achievers.ieps.backend.repository.UserDb;
import achievers.ieps.backend.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BerkasServiceImpl implements BerkasService {
    @Autowired
    private BerkasDb berkasDb;
    @Autowired
    private UserDb userDb;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Berkas store(CreateBerkasRequestDTO createBerkasRequestDTO) throws IOException {
        var email = jwtUtils.getEmailFromJwtToken(createBerkasRequestDTO.getToken());
        Vendor vendor = (Vendor) userDb.findByEmail(email);

        Berkas berkas = new Berkas();
        berkas.setNama(createBerkasRequestDTO.getNama());
        berkas.setDeskripsi(createBerkasRequestDTO.getDeskripsi());
        berkas.setJudul(createBerkasRequestDTO.getJudul());
        berkas.setType(createBerkasRequestDTO.getType());
        berkas.setData(createBerkasRequestDTO.getData());
        berkas.setVendor(vendor);

        return berkasDb.save(berkas);
    }

    @Override
    public Berkas getBerkas(UUID vendorId, Long berkasId) {
        return berkasDb.findByVendorIdAndId(vendorId, berkasId);
    }

    @Override
    public List<Berkas> getAllBerkas(String id) {
        UUID vendorId = UUID.fromString(id); // Assuming id is a String representation of the vendorId
        return berkasDb.findByVendorId(vendorId);
    }

    @Override
    public void deleteBerkasVendor(String token) {
        var email = jwtUtils.getEmailFromJwtToken(token);
        Vendor vendor = (Vendor) userDb.findByEmail(email);
        List<Berkas> listBerkas = berkasDb.findByVendorId(vendor.getId());

        for (Berkas berkas : listBerkas) {
            berkasDb.delete(berkas);
        }
    }

    @Override
    public Berkas edit(UpdateBerkasRequestDTO updateBerkasRequestDTO) {
        var email = jwtUtils.getEmailFromJwtToken(updateBerkasRequestDTO.getToken());
        Vendor vendor = (Vendor) userDb.findByEmail(email);
        System.out.println(vendor.getId()); System.out.println(updateBerkasRequestDTO.getId());
        var berkasExisting = berkasDb.findByVendorIdAndId(
                vendor.getId(), updateBerkasRequestDTO.getId());

        berkasExisting.setJudul(updateBerkasRequestDTO.getJudul());
        berkasExisting.setType(updateBerkasRequestDTO.getType());
        berkasExisting.setData(updateBerkasRequestDTO.getData());

        return berkasDb.save(berkasExisting);
    }

}
