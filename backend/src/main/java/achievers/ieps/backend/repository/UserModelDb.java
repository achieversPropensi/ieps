package achievers.ieps.backend.repository;


import achievers.ieps.backend.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserModelDb extends JpaRepository<UserModel, UUID> {
    List<UserModel> findAll();
    List<UserModel> findAllByIsDeletedFalse();
}
