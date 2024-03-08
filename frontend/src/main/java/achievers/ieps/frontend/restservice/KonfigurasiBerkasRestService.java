package achievers.ieps.frontend.restservice;

import java.util.List;

import org.springframework.http.ResponseEntity;

import achievers.ieps.frontend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;

public interface KonfigurasiBerkasRestService {
    List<KonfigurasiBerkasResponseDTO> getAllKonfigurasiBerkas(String token);
    ResponseEntity<String> addKonfigurasiBerkas(List<CreateKonfigurasiBerkasRequestDTO> listKonfigurasiBerkas, String token); 
}
