package achievers.ieps.backend.restcontroller;

import achievers.ieps.backend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.backend.dto.response.VendorInfoResponseDTO;
import achievers.ieps.backend.security.jwt.JwtUtils;
import achievers.ieps.backend.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorRestController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    VendorService vendorService;

    @PostMapping("/info")
    public ResponseEntity<VendorInfoResponseDTO> getUserInfo(@RequestBody LoginJwtResponseDTO tokenDTO){
        String email = jwtUtils.getEmailFromJwtToken(tokenDTO.getToken());
        var vendor = vendorService.getVendorByEmail(email);

        var vendorInfoDTO = new VendorInfoResponseDTO();
        vendorInfoDTO.setId(vendor.getId());
        vendorInfoDTO.setStatus(vendor.getStatus());
        vendorInfoDTO.setHasSubmitted(vendor.isHasSubmitted());

        return ResponseEntity.ok().body(vendorInfoDTO);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, String>> toggleSubmitStatus(@RequestBody LoginJwtResponseDTO tokenDTO){
        String email = jwtUtils.getEmailFromJwtToken(tokenDTO.getToken());
        Map<String, String> response = new HashMap<>();

        vendorService.toggleSubmission(email);
        var vendor = vendorService.getVendorByEmail(email);
        if (!vendor.isHasSubmitted()) {
            response.put("submissionStatus", "Vendor has not submitted");
            return ResponseEntity.ok().body(response);
        }
        response.put("submissionStatus", "Vendor has submitted");
        return ResponseEntity.ok().body(response);
    }
}
