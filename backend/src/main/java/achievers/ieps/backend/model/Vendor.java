package achievers.ieps.backend.model;
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
@Table(name = "vendor")
public class Vendor extends UserModel {
    @NotNull
    @Column(name = "alamat")
    private String alamat;

    @NotNull
    @Column(name = "namaPerusahaan")
    private String namaPerusahaan;

    @NotNull
    @Column(name = "status")
    private String status;
}
