package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.Berkas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BerkasDb extends JpaRepository<Berkas, Long> {
    List<Berkas> findByVendorId(UUID vendorId);
    Berkas findByVendorIdAndId(UUID vendorId, Long berkasId);
}
