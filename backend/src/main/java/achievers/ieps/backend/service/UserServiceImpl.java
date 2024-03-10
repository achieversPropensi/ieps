package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.model.Admin;
import achievers.ieps.backend.model.ProcManager;
import achievers.ieps.backend.model.ProcStaff;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;
import achievers.ieps.backend.repository.AdminDb;
import achievers.ieps.backend.repository.ProcManagerDb;
import achievers.ieps.backend.repository.ProcStaffDb;
import achievers.ieps.backend.repository.UserDb;
import achievers.ieps.backend.repository.UserModelDb;
import achievers.ieps.backend.repository.VendorDb;
import achievers.ieps.backend.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AdminDb adminDb;

    @Autowired
    private ProcManagerDb procManagerDb;

    @Autowired
    private ProcStaffDb procStaffDb;

    @Autowired
    private VendorDb vendorDb;

    @Autowired
    private UserModelDb userModelDb;

    @Override
    public UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO){
        if (user instanceof Vendor){
            Vendor vendor = (Vendor) user;
            vendor.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
            String hashedPass = encrypt(vendor.getPassword());
            vendor.setPassword(hashedPass);
            vendor.setCreatedAt(LocalDateTime.now());
            vendor.setUpdatedAt(LocalDateTime.now());
            vendor.setStatus("Belum Terverifikasi");
            return userDb.save(vendor);
        }
        user.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
        String hashedPass = encrypt(user.getPassword());
        user.setPassword(hashedPass);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userDb.save(user);
    }

    @Override
    public HashMap<String, String> getUserInfo(String email) {
        UserModel userModel = userDb.findByEmail(email);
        HashMap<String, String> userInfo = new HashMap<>();
        if (userModel instanceof Vendor){
            Vendor vendor = (Vendor) userModel;
            userInfo.put("id", vendor.getId().toString());
            userInfo.put("email", vendor.getEmail());
            userInfo.put("nama perusahaan", vendor.getNama());
            userInfo.put("password", vendor.getPassword());
            userInfo.put("telefon", vendor.getNomorTelefon());
            userInfo.put("status", vendor.getStatus());
            userInfo.put("alamat", vendor.getAlamat());
        } else {
            userInfo.put("id", userModel.getId().toString());
            userInfo.put("email", userModel.getEmail());
            userInfo.put("nama perusahaan", userModel.getNama());
            userInfo.put("password", userModel.getPassword());
            userInfo.put("telefon", userModel.getNomorTelefon());
        }
        return userInfo;
    }

    @Override
    public Vendor vendorDetails(UUID id){
        for (Vendor vendor: getAllVendors()){
            if (vendor.getId().equals(id)){
                return vendor;
            }
        }
        return null;
    }

    @Override
    public Vendor setUpdateStatus(UUID id, String status){
        Vendor vendor = vendorDetails(id);
        if (status.equals("Berhasil")){
            vendor.setStatus("Sudah Terverifikasi");
        } else {
            vendor.setStatus("Gagal Terverifikasi");
        }
        userDb.save(vendor);
        return vendor;
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public ResponseEntity<String> loginJwt(LoginJwtRequestDTO loginJwtRequestDTO){
        String email = loginJwtRequestDTO.getEmail();
        UserModel userModel = userDb.findByEmail(email);
        if (userModel == null || !(verifyPassword(loginJwtRequestDTO.getPassword(), userModel.getPassword()))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(jwtUtils.generateJwtToken(loginJwtRequestDTO.getEmail()));
    }

    @Override
    public String checkUser(@Valid @RequestBody String email){
        var user = userDb.findByEmail(email);
        if (user.getRole().getRole().equals("Vendor")){
            return "Vendor";
        } else if (user.getRole().getRole().equals("Admin")){
            return "Admin";
        } else if (user.getRole().getRole().equals("Procurement Staff")){
            return "Procurement Staff";
        } else if (user.getRole().getRole().equals("Procurement Manager")){
            return "Procurement Manager";
        } else {
            return null;
        }
    }

    @Override
    public String checkVendorStatus(@Valid @RequestBody String email){
        UserModel userModel = userDb.findByEmail(email);
        if (userModel instanceof Vendor){
            Vendor vendor = (Vendor) userModel;
            return vendor.getStatus();
        }
        return null;
    }


    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public List<UserModel> getAllUser(){
        return userDb.findAll();
    }

    @Override
    public List<Vendor> getAllVendors(){
        List<Vendor> listVendor = new ArrayList<>();
        for (UserModel user : getAllUser()){
            if (user.getRole().getRole().equals("Vendor")){
                Vendor vendor = (Vendor) user;
                listVendor.add(vendor);
            }
        }
        return listVendor;
    }


    @Override
    public boolean isEmailExist(String email){
        return getAllUser().stream().anyMatch(b -> b.getEmail().equals(email));
    }

    @Override
    public boolean isEmailExist(String email, UUID id){
        return getAllUser().stream().anyMatch(b -> b.getEmail().equals(email) && b.getId().equals(id));
    }

    @Override
    public boolean isPasswordExist(String password){
        return getAllUser().stream().anyMatch(b -> b.getPassword().equals(password));
    }

    @Override
    public boolean isPasswordExist(String password, UUID id){
        return getAllUser().stream().anyMatch(b -> b.getPassword().equals(password) && !b.getId().equals(id));
    }

    // PBI-6 Mengelola Profil (U)
    @Override
    public UserModel updateUser(UserModelResponseDTO userModelResponseDTO){
        String role = userModelResponseDTO.getRole();
        UUID id = UUID.fromString(userModelResponseDTO.getId());
        String newEmail = userModelResponseDTO.getEmail();
        String emailToken = userModelResponseDTO.getEmailToken();

        if (newEmail.equals(emailToken)) {
            if (role.equals("Admin")) {
                Admin existingUser = adminDb.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));
                existingUser.setNama(userModelResponseDTO.getNama());
                existingUser.setEmail(userModelResponseDTO.getEmail());
                existingUser.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                existingUser.setUpdatedAt(LocalDateTime.now());

                return adminDb.save(existingUser);

            }else if (role.equals("Procurement Manager")){
                ProcManager existingProcManager = procManagerDb.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                existingProcManager.setNama(userModelResponseDTO.getNama());
                existingProcManager.setEmail(userModelResponseDTO.getEmail());
                existingProcManager.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                existingProcManager.setUpdatedAt(LocalDateTime.now());


                return procManagerDb.save(existingProcManager);

            }else if (role.equals("Procurement Staff")){
                ProcStaff existingProcStaff = procStaffDb.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                existingProcStaff.setNama(userModelResponseDTO.getNama());
                existingProcStaff.setEmail(userModelResponseDTO.getEmail());
                existingProcStaff.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                existingProcStaff.setUpdatedAt(LocalDateTime.now());

                return procStaffDb.save(existingProcStaff);
            } else if (role.equals("Vendor")){
                Vendor existingVendor = vendorDb.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));
                existingVendor.setNama(userModelResponseDTO.getNama());
                existingVendor.setEmail(userModelResponseDTO.getEmail());
                existingVendor.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                existingVendor.setAlamat(userModelResponseDTO.getAlamat());
                existingVendor.setNamaPerusahaan(userModelResponseDTO.getNamaPerusahaan());
                existingVendor.setUpdatedAt(LocalDateTime.now());

                return vendorDb.save(existingVendor);
            }
            else {
                throw new NoSuchElementException("Role not found");
            }
        } else {
            if (!isEmailExist(newEmail)){
                if (role.equals("Admin")) {
                    Admin existingUser = adminDb.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("User not found"));
                    existingUser.setNama(userModelResponseDTO.getNama());
                    existingUser.setEmail(userModelResponseDTO.getEmail());
                    existingUser.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                    existingUser.setUpdatedAt(LocalDateTime.now());

                    return adminDb.save(existingUser);

                }else if (role.equals("Procurement Manager")){
                    ProcManager existingProcManager = procManagerDb.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("User not found"));

                    existingProcManager.setNama(userModelResponseDTO.getNama());
                    existingProcManager.setEmail(userModelResponseDTO.getEmail());
                    existingProcManager.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                    existingProcManager.setUpdatedAt(LocalDateTime.now());


                    return procManagerDb.save(existingProcManager);

                }else if (role.equals("Procurement Staff")){
                    ProcStaff existingProcStaff = procStaffDb.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("User not found"));

                    existingProcStaff.setNama(userModelResponseDTO.getNama());
                    existingProcStaff.setEmail(userModelResponseDTO.getEmail());
                    existingProcStaff.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                    existingProcStaff.setUpdatedAt(LocalDateTime.now());

                    return procStaffDb.save(existingProcStaff);
                } else if (role.equals("Vendor")){
                    Vendor existingVendor = vendorDb.findById(id)
                            .orElseThrow(() -> new NoSuchElementException("User not found"));
                    existingVendor.setNama(userModelResponseDTO.getNama());
                    existingVendor.setEmail(userModelResponseDTO.getEmail());
                    existingVendor.setNomorTelefon(userModelResponseDTO.getNomorTelefon());
                    existingVendor.setAlamat(userModelResponseDTO.getAlamat());
                    existingVendor.setNamaPerusahaan(userModelResponseDTO.getNamaPerusahaan());
                    existingVendor.setUpdatedAt(LocalDateTime.now());

                    return vendorDb.save(existingVendor);
                }
                else {
                    throw new NoSuchElementException("Role not found");
                }
            }
        }
        return null;
    }

    // PBI-6 (R)
    @Override
    public UserModelResponseDTO getUserByEmail(String email){
        var user = userModelDb.findByEmail(email);
        UserModelResponseDTO userModelDTO = new UserModelResponseDTO();

        if (user.isPresent() && !user.get().isDeleted()) {
            userModelDTO.setId(user.get().getId().toString());
            userModelDTO.setNama(user.get().getNama());
            userModelDTO.setEmail(user.get().getEmail());
            userModelDTO.setRole(user.get().getRole().getRole());
            userModelDTO.setNomorTelefon(user.get().getNomorTelefon());
            userModelDTO.setPassword(user.get().getPassword());
            if (user.get() instanceof Vendor){
                userModelDTO.setAlamat(((Vendor) user.get()).getAlamat());
                userModelDTO.setNamaPerusahaan(((Vendor) user.get()).getNamaPerusahaan());
                userModelDTO.setStatus(((Vendor) user.get()).getStatus());
            }
            return userModelDTO;
        }
        else{
            throw new NoSuchElementException("User Not Found");
        }
    }

    // PBI-6 (U Password)
    @Override
    public UserModelResponseDTO updatePasswordUser(String email, String password, String confirmPassword) {
        UserModel userModel = userDb.findByEmail(email);
        if (userModel != null && password.equals(confirmPassword)) {
            var oldPassword = userModel.getPassword();
            boolean isSamePassword = checkPassword(password, oldPassword);
            if (!isSamePassword){
                userModel.setPassword(encrypt(password));
                userModelDb.save(userModel);
                return getUserByEmail(email);
            }
        }
        return null;
    }
}
