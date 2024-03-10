package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO);
    String encrypt(String password);
    ResponseEntity<String> loginJwt(LoginJwtRequestDTO loginJwtRequestDTO);
    boolean verifyPassword(String rawPassword, String hashedPassword);
    List<UserModel> getAllUser();
    List<Vendor> getAllVendors();
    String checkUser(String email);
    HashMap<String, String> getUserInfo(String username);
    Vendor vendorDetails(UUID id);
    Vendor setUpdateStatus(UUID id, String status);
    String checkVendorStatus(@Valid @RequestBody String email);
    boolean isEmailExist(String email);
    boolean isEmailExist(String email, UUID id);
    boolean isPasswordExist(String password);
    boolean isPasswordExist(String password, UUID id);
    UserModel updateUser(UserModelResponseDTO userModelResponseDTO); //PBI-6 (U)
    UserModelResponseDTO getUserByEmail(String email); // PBI-6 (R)
    UserModelResponseDTO updatePasswordUser(String email, String password, String confirmPassword);
    boolean checkPassword(String rawPassword, String encodedPassword);
    
}
