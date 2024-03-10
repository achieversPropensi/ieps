package achievers.ieps.frontend.restservice;

import achievers.ieps.frontend.dto.response.VendorInfoResponseDTO;

import java.util.Map;

public interface VendorRestService {
    VendorInfoResponseDTO checkInfo(String token);
    Map<String, String> toggleSubmission(String token);
}
