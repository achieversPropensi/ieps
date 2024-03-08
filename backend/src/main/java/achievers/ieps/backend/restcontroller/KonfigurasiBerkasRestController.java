package achievers.ieps.backend.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import achievers.ieps.backend.dto.KonfigurasiBerkasMapper;
import achievers.ieps.backend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.backend.model.KonfigurasiBerkas;
import achievers.ieps.backend.service.KonfigurasiBerkasService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/konfigurasi-berkas")
public class KonfigurasiBerkasRestController {

    @Autowired
    KonfigurasiBerkasMapper konfigurasiBerkasMapper;

    @Autowired
    KonfigurasiBerkasService konfigurasiBerkasService;

    @PutMapping("/add")
    public ResponseEntity<String> restAddKonfigurasiBerkas(@Valid @RequestBody List<CreateKonfigurasiBerkasRequestDTO> listKonfigurasiBerkasRequestDTO, BindingResult bindingResult) {
        try {
            List<KonfigurasiBerkas> lstKB = new ArrayList<>();
            for (CreateKonfigurasiBerkasRequestDTO dto : listKonfigurasiBerkasRequestDTO){
                lstKB.add(konfigurasiBerkasMapper.createKonfigurasiBerkasRequestDTOToKonfigurasiBerkas(dto));
            }
            return konfigurasiBerkasService.addKonfigurasiBerkas(lstKB);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan internal server.");
            // TODO: handle 400 dan input body ngaco
        }
    }


    @GetMapping(value = "/get")
    public List<KonfigurasiBerkas> restRetrieveKonfigurasiBerkas() {
        return konfigurasiBerkasService.getAllKonfigurasiBerkas();
    }    

}

