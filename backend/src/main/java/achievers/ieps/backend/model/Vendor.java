package achievers.ieps.backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    List<Berkas> listBerkas;

    @NotNull
    @Column(name = "has_submitted")
    @JsonIgnore
    private boolean hasSubmitted = Boolean.FALSE;
}
