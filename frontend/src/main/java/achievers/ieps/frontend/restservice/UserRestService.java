package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.frontend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.frontend.dto.response.VendorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserRestService {
    String registerVendor(@Valid @ModelAttribute CreateVendorRequestDTO vendorDTO) throws IOException, InterruptedException;
    String getToken(HttpServletRequest request, String email, String password) throws IOException, InterruptedException, JSONException;
    VendorResponseDTO getProfilVendor(String token, UUID id) throws IOException, InterruptedException;
    VendorResponseDTO updateProfilVendor(String status, String token, UUID id) throws IOException, InterruptedException;
    List<VendorResponseDTO> getAllVendor() throws IOException, InterruptedException;
    String getUserInfo(LoginJwtResponseDTO token) throws IOException, InterruptedException;
}
