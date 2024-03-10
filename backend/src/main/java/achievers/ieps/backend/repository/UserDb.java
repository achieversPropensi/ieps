package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDb extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);
}
