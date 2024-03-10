package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.request.CreateBerkasRequestDTO;
import achievers.ieps.frontend.dto.response.BerkasInfoResponseDTO;
import achievers.ieps.frontend.dto.response.KonfigurasiBerkasResponseDTO;
import jakarta.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BerkasRestServiceImpl implements BerkasRestService {
    private final WebClient mockWebClient;
    private final WebClient webClient;
    private final String backendUrl = "http://localhost:8080/api";
    private final String mockUrl = "https://78cf56a3-7e45-4d24-a1f6-ee86bbb016a4.mock.pstmn.io/api";

    @Autowired
    VendorRestService vendorRestService;

    public BerkasRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.mockWebClient = webClientBuilder
                .baseUrl(mockUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient = webClientBuilder
                .baseUrl(backendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }



    @Override
    public List<KonfigurasiBerkasResponseDTO> retrieveKonfigurasi(String token) {

        var response = this.mockWebClient
                .get()
                .uri("/konfigurasi-berkas/get")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference
                        <List<KonfigurasiBerkasResponseDTO>>() {})
                .block();

        return response;
    };

    @Override
    public List<BerkasInfoResponseDTO> retrieveAllBerkas(String token) {
        var id = vendorRestService.checkInfo(token).getId();
        var response = this.webClient
                .get()
                .uri("/berkas/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference
                        <List<BerkasInfoResponseDTO>>() {})
                .block();

        return response;
    };

    @Override
    public Map<String, String> uploadBerkas(String token, CreateBerkasRequestDTO uploadBerkasDTO) {
        var id = vendorRestService.checkInfo(token).getId();
        var response = this.webClient
                .post()
                .uri("/berkas/upload")
                .header("Authorization", "Bearer " + token)
                .bodyValue(uploadBerkasDTO)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response;
    };

    @Override
    public Map<String, String> updateBerkas(String token, CreateBerkasRequestDTO uploadBerkasDTO) {
        var id = vendorRestService.checkInfo(token).getId();

        var response = this.webClient
                .post()
                .uri("/berkas/update")
                .header("Authorization", "Bearer " + token)
                .bodyValue(uploadBerkasDTO)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response;
    };

    @Override
    public List<BerkasInfoResponseDTO> retrieveAllBerkasById(String token, String id) {
        var response = this.webClient
                .get()
                .uri("/berkas/" + id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference
                        <List<BerkasInfoResponseDTO>>() {})
                .block();

        return response;
    };

}
