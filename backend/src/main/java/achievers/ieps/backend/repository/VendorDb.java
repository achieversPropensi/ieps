package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorDb extends JpaRepository<Vendor, UUID> {
}
