package achievers.ieps.backend.repository;

import achievers.ieps.backend.model.KonfigurasiBerkas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KonfigurasiBerkasDb extends JpaRepository<KonfigurasiBerkas, String> {

}
