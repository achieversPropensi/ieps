package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.frontend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.frontend.dto.request.PasswordRequestDTO;
import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.dto.response.UserModelResponseDTO;
import achievers.ieps.frontend.dto.response.VendorResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserRestServiceImpl implements UserRestService {
    private final WebClient webClient;
    private final String backendUrl = "http://localhost:8080/api/";
    public UserRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(backendUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String registerVendor(@Valid @ModelAttribute CreateVendorRequestDTO vendorDTO) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        vendorDTO.setRole("Vendor");
        String requestBody = objectMapper.writeValueAsString(vendorDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "user/create-vendor"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return output.body();
    }

    @Override
    public String getToken(HttpServletRequest request, String email, String password) throws IOException, InterruptedException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new LoginJwtRequestDTO(email, password));
        HttpRequest response = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "auth/login-jwt"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(response, HttpResponse.BodyHandlers.ofString());

        if (output.statusCode() == 400) {
            return null;
        }

        var token = output.body();
        JsonNode jsonResponse = objectMapper.readTree(token);
        token = jsonResponse.get("body").asText();

        HttpRequest responseRole = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "user/check-user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(email))
                .build();
        HttpResponse<String> outputRole = HttpClient.newHttpClient().send(responseRole, HttpResponse.BodyHandlers.ofString());

        if (outputRole.statusCode() == 400){
            return null;
        }

        var userRole = outputRole.body();

        HttpRequest responseStatus = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "user/check-user-status"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(email))
                .build();
        HttpResponse<String> outputStatus = HttpClient.newHttpClient().send(responseStatus, HttpResponse.BodyHandlers.ofString());

        if (outputStatus.statusCode() == 400){
            return null;
        }
        var userStatus = outputStatus.body();

        if (userRole.equals("Vendor")){
            if (userStatus.equals("Belum Terverifikasi") || userStatus.equals("Gagal Terverifikasi")){
                List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("VENDOR_NOT_VALID"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "VENDOR_NOT_VALID", authorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                httpSession.setAttribute("token", token);

            } else {
                List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("VENDOR"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "VENDOR", authorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                httpSession.setAttribute("token", token);
            }
        } else if (userRole.equals("Admin")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "ADMIN", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token);
        } else if (userRole.equals("Procurement Staff")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("PROCSTAFF"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "PROCSTAFF", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token);
        } else if (userRole.equals("Procurement Manager")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("PROCMANAGER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "PROCMANAGER", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token);
        }
        return token;
    }

    @Override
    public String getUserInfo(LoginJwtResponseDTO token) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(token);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "user/info"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(output.body());
        return output.body();
    }

    @Override
    public List<VendorResponseDTO> getAllVendor() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UserRestServiceImpl.this.backendUrl + "user/get-vendors"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        List<VendorResponseDTO> vendors = objectMapper.readValue(output.body(), new TypeReference<List<VendorResponseDTO>>(){});
        return vendors;
    }

    @Override
    public VendorResponseDTO getProfilVendor(String token, UUID id) throws IOException, InterruptedException {
        String url = UserRestServiceImpl.this.backendUrl + "user/detail-profil/" + id.toString();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        var body = output.body();
        ObjectMapper objectMapper = new ObjectMapper();
        VendorResponseDTO vendor = objectMapper.readValue(body, new TypeReference<VendorResponseDTO>(){});
        return vendor;
    }

    @Override
    public VendorResponseDTO updateProfilVendor(String status, String token, UUID id) throws IOException, InterruptedException{
        String url = UserRestServiceImpl.this.backendUrl + "user/detail-profil/" + id.toString() + "/validasi";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(status))
                .build();
        HttpResponse<String> output = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        var body = output.body();
        ObjectMapper objectMapper = new ObjectMapper();
        VendorResponseDTO vendor = objectMapper.readValue(body, new TypeReference<VendorResponseDTO>(){});
        return vendor;
    }

    @Override
    public UserModelResponseDTO viewProfile(LoginJwtResponseDTO token) throws IOException, InterruptedException{
        var response = this.webClient
                .post()
                .uri("user/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
                .bodyValue(token)
                .retrieve()
                .bodyToMono(UserModelResponseDTO.class)
                .block();
        return response;
    }

    @Override
    public UserModelResponseDTO updateProfilePassword(PasswordRequestDTO passwordRequestDTO, String token) throws IOException, InterruptedException{
        var response = this.webClient
                .put()
                .uri("user/profile/edit/password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(passwordRequestDTO)
                .retrieve()
                .bodyToMono(UserModelResponseDTO.class)
                .block();
        return response;
    }

    @Override
    public UserModelResponseDTO updateProfile(HttpServletRequest request, UserModelResponseDTO userModelResponseDTO, String token) throws IOException, InterruptedException{
        updateSession(userModelResponseDTO.getStatus(), userModelResponseDTO.getRole(), userModelResponseDTO.getEmail(), userModelResponseDTO.getPassword(), token, request);
        var response = this.webClient
                .put()
                .uri("user/profile/edit")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(userModelResponseDTO)
                .retrieve()
                .bodyToMono(UserModelResponseDTO.class)
                .block();
        return response;
    }

    @Override
    public void updateSession(String status, String role, String email, String password, String tokenDTO, HttpServletRequest httprequest) throws IOException, InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");
        LoginJwtRequestDTO loginDTO = new LoginJwtRequestDTO(email, password);
        LoginJwtResponseDTO token = client.post()
                .uri("/api/auth/token")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO)
                .bodyValue(loginDTO)
                .retrieve()
                .bodyToMono(LoginJwtResponseDTO.class)
                .block();
        assert token != null;
        if (role.equals("Vendor")){
            if (status.equals("Belum Terverifikasi") || status.equals("Gagal Terverifikasi")){
                List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("VENDOR_NOT_VALID"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "VENDOR_NOT_VALID", authorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession httpSession = httprequest.getSession(false);
                if (httpSession != null) {
                    httpSession.invalidate();
                }
                httpSession = httprequest.getSession(true);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                httpSession.setAttribute("token", token.getToken());

            } else {
                List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("VENDOR"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "VENDOR", authorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession httpSession = httprequest.getSession(false);
                if (httpSession != null) {
                    httpSession.invalidate();
                }
                httpSession = httprequest.getSession(true);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
                httpSession.setAttribute("token", token.getToken());
            }
        } else if (role.equals("Admin")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "ADMIN", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = httprequest.getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            httpSession = httprequest.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token.getToken());
        } else if (role.equals("Procurement Staff")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("PROCSTAFF"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "PROCSTAFF", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = httprequest.getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            httpSession = httprequest.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token.getToken());
        } else if (role.equals("Procurement Manager")){
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("PROCMANAGER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, "PROCMANAGER", authorities);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession httpSession = httprequest.getSession(false);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            httpSession = httprequest.getSession(true);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            httpSession.setAttribute("token", token.getToken());
        }
    }
}
