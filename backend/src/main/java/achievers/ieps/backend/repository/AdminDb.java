package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminDb extends JpaRepository<Admin, UUID> {
}
