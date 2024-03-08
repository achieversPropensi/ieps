package achievers.ieps.backend.model;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "konfigurasi_berkas")
public class KonfigurasiBerkas {
    
    @Id
    @Column(name = "berkas_id")
    private UUID berkasId = UUID.randomUUID();

    @NotNull
    @Column(name = "nama_berkas")
    private String namaBerkas;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
}
