package achievers.ieps.backend.restController;

import achievers.ieps.backend.dto.UserMapper;
import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.backend.dto.request.PasswordRequestDTO;
import achievers.ieps.backend.dto.response.CreateUserResponseDTO;
import achievers.ieps.backend.dto.response.LoginJwtResponseDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.dto.response.VendorResponseDTO;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;
import achievers.ieps.backend.security.jwt.JwtUtils;
import achievers.ieps.backend.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/user")
public class UserRestController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    @GetMapping("/get-vendors")
    public ResponseEntity<List<Vendor>> getAllVendors(){
        var listVendor = userService.getAllVendors();
        return ResponseEntity.ok(listVendor);
    }

    @GetMapping("/detail-profil/{id}")
    public ResponseEntity<VendorResponseDTO> detailProfil(@PathVariable UUID id){
        Vendor vendor = userService.vendorDetails(id);
        var vendorDTO = userMapper.vendorResponseDTOToVendor(vendor);
        vendorDTO.setRole(vendor.getRole().getRole());
        return ResponseEntity.ok(vendorDTO);
    }

    @PutMapping("/detail-profil/{id}/validasi")
    public ResponseEntity<VendorResponseDTO> updateStatusProfil(@PathVariable UUID id, @RequestBody String status){
        var vendor = userService.setUpdateStatus(id, status);
        var vendorDTO = userMapper.vendorResponseDTOToVendor(vendor);
        vendorDTO.setRole(vendor.getRole().getRole());
        return ResponseEntity.ok(vendorDTO);
    }

    @PostMapping("/info")
    public ResponseEntity<Map<String, String>> getUserInfo(@RequestBody LoginJwtResponseDTO tokenDTO){
        String email = jwtUtils.getEmailFromJwtToken(tokenDTO.getToken());
        var userInfo = userService.getUserInfo(email);
        for (Map.Entry<String, String> entry : userInfo.entrySet()) {
            System.out.println(entry.getKey() + " :  " + entry.getValue());
        }
        return ResponseEntity.ok(userInfo);
    }


    @PostMapping(value = "/create-user")
    public ResponseEntity<?> restAddUser(@Valid @RequestBody CreateUserRequestDTO userDTO) {
        try {
            var user = userMapper.createUserRequestDTOToUser(userDTO);
            CreateUserResponseDTO responseDTO = userMapper.createUserResponseDTOToUserModel(user);
            responseDTO.setRole(user.getRole().getRole());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/check-user")
    public String checkUser(@RequestBody String token){
        return userService.checkUser(token);
    }

    @PostMapping(value = "/check-user-status")
    public String checkVendorStatus(@RequestBody String email){
        return userService.checkVendorStatus(email);
    }

    @PostMapping(value = "/create-vendor")
    public UserModel restAddVendor(@Valid @RequestBody CreateVendorRequestDTO vendorDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            var user = userMapper.createVendorRequestDTOToVendor(vendorDTO);
            if (userService.isEmailExist(user.getEmail())){
                return null;
            } else {
                return userService.addUser(user, vendorDTO);
            }
        }
    }

    // PBI-6 (R)
    @PostMapping(value="/profile")
    public UserModelResponseDTO viewProfile(@RequestBody LoginJwtResponseDTO tokenDTO){
        String email = jwtUtils.getEmailFromJwtToken(tokenDTO.getToken());
        var user = userService.getUserByEmail(email);
        return user;
    }

    // PBI-6 (U) Password (Blm dihandle)
    @PutMapping(value="/profile/edit/password")
    public UserModelResponseDTO updateProfilePassword(@RequestBody PasswordRequestDTO passwordRequestDTO) {
        return userService.updatePasswordUser(passwordRequestDTO.getEmail(), passwordRequestDTO.getPasswordBaru(), passwordRequestDTO.getPasswordBaruKonfirmasi());
    } 

    // PBI-6 (U) Blm dihandle
    @PutMapping(value="/profile/edit")
    public UserModelResponseDTO updateProfile(@RequestBody UserModelResponseDTO userModelResponseDTO) {
        var user = userService.updateUser(userModelResponseDTO);
        if (user != null){
            UserModelResponseDTO userDTO = new UserModelResponseDTO();
            userDTO.setId(String.valueOf(user.getId()));
            userDTO.setNama(user.getNama());
            userDTO.setRole(user.getRole().getRole());
            userDTO.setNomorTelefon(user.getNomorTelefon());
            userDTO.setPassword(user.getPassword());
            userDTO.setEmail(user.getEmail());

            if (user instanceof Vendor){
                Vendor vendor = (Vendor) user;
                userDTO.setStatus(vendor.getStatus());
                userDTO.setAlamat(vendor.getAlamat());
                userDTO.setNamaPerusahaan(vendor.getNamaPerusahaan());
            }
            return userDTO;
        }
        return null;
    }
}
