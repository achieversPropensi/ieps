package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.dto.response.VendorInfoResponseDTO;
import jakarta.transaction.Transactional;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Transactional
public class VendorRestServiceImpl implements VendorRestService {
    private final WebClient webClient;
    private final String backendUrl = "http://localhost:8080/api";

    public VendorRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(backendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Override
    public VendorInfoResponseDTO checkInfo(String token) {
        var response = this.webClient
                .post()
                .uri("/vendor/info")
                .header("Authorization", "Bearer " + token)
                .bodyValue(new LoginJwtResponseDTO(token))
                .retrieve()
                .bodyToMono(VendorInfoResponseDTO.class)
                .block();

        return response;
    };

    @Override
    public Map<String, String> toggleSubmission(String token) {
        var response = this.webClient
                .post()
                .uri("/vendor/toggle")
                .header("Authorization", "Bearer " + token)
                .bodyValue(new LoginJwtResponseDTO(token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, String> message = (Map<String, String>) response;
        return message;
    };

}
