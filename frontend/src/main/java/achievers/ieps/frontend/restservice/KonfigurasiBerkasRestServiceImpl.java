package achievers.ieps.frontend.restservice;

import org.apache.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import achievers.ieps.frontend.dto.request.CreateKonfigurasiBerkasRequestDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class KonfigurasiBerkasRestServiceImpl implements KonfigurasiBerkasRestService{
    private final WebClient webClient;
    private final String backendUrl = "http://localhost:8080/api/konfigurasi-berkas";

    public KonfigurasiBerkasRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(backendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<KonfigurasiBerkasResponseDTO> getAllKonfigurasiBerkas(String token) {
        List<KonfigurasiBerkasResponseDTO> responseList = new ArrayList<>();
        
        List<KonfigurasiBerkasResponseDTO> response = this.webClient
                .get()
                .uri("/get")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(KonfigurasiBerkasResponseDTO.class)
                .collectList()
                .block();
    
        if (response != null) {
            responseList.addAll(response);
        }
        return responseList;
    }
    

    @Override
    public ResponseEntity<String> addKonfigurasiBerkas(List<CreateKonfigurasiBerkasRequestDTO> listKonfigurasiBerkas, String token){
        var response = this.webClient
                .put()
                .uri("/add")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(listKonfigurasiBerkas)
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> ResponseEntity.ok("Successfully added konfigurasi berkas.")) // Map the response body to a success message
                .block();
        
        return response;
    }

    
}
