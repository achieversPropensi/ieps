package achievers.ieps.backend.service;


import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.LoginJwtRequestDTO;
import achievers.ieps.backend.model.Role;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;
import achievers.ieps.backend.repository.UserDb;
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
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;

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
        return getAllUser().stream().anyMatch(b -> b.getEmail().equals(email) && !b.getId().equals(id));
    }

    @Override
    public boolean isPasswordExist(String password){
        return getAllUser().stream().anyMatch(b -> b.getPassword().equals(password));
    }

    @Override
    public boolean isPasswordExist(String password, UUID id){
        return getAllUser().stream().anyMatch(b -> b.getPassword().equals(password) && !b.getId().equals(id));
    }
}
