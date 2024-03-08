package achievers.ieps.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "procManager")
public class ProcManager extends UserModel {
}
