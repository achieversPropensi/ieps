package achievers.ieps.backend.service;

import achievers.ieps.backend.model.KonfigurasiBerkas;
import java.util.List;

import org.springframework.http.ResponseEntity;

public interface KonfigurasiBerkasService {
    List<KonfigurasiBerkas> getAllKonfigurasiBerkas();
    ResponseEntity<String> addKonfigurasiBerkas(List<KonfigurasiBerkas> listKonfigurasiBerkas);
    
}
