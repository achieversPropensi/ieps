package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VendorDb extends JpaRepository<Vendor, UUID> {
    Vendor findByEmail(String email);
}
