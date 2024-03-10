package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleDb extends JpaRepository<Role, UUID> {
    List<Role> findAll();
    Optional<Role> findByRole(String role);
}
