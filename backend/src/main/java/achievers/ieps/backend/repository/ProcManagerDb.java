package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.ProcManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcManagerDb extends JpaRepository<ProcManager, UUID> {
}
