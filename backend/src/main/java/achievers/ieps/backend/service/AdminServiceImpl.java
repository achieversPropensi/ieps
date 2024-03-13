package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.AdminCreateUserRequestDTO;
import achievers.ieps.backend.dto.request.AdminUpdateUserRequestDTO;
import achievers.ieps.backend.dto.request.UpdateVendorRequestDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.dto.response.VendorResponseDTOFauzan;
import achievers.ieps.backend.model.*;
import achievers.ieps.backend.repository.*;
import achievers.ieps.backend.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserModelDb userModelDb;

    @Autowired
    private AdminDb adminDb;

    @Autowired
    private ProcManagerDb procManagerDb;

    @Autowired
    private ProcStaffDb procStaffDb;

    @Autowired
    private VendorDb vendorDb;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    // Get All User
    @Override
    @Transactional
    public List<UserModelResponseDTO> getAllUserModel() {

        List<UserModel> users = userModelDb.findAllByIsDeletedFalse();
        List<UserModelResponseDTO> listUserModelDTO = new ArrayList<>();

        for (UserModel userModel : users) {
            UserModelResponseDTO userModelDTO = new UserModelResponseDTO();

            userModelDTO.setId(userModel.getId().toString());
            userModelDTO.setNama(userModel.getNama());
            userModelDTO.setEmail(userModel.getEmail());
            userModelDTO.setRole(userModel.getRole().getRole());

            listUserModelDTO.add(userModelDTO);
        }
        return listUserModelDTO;
    }

    // Get User By ID (Untuk pass ke FE, jadi pakai dto)
    @Override
    public UserModelResponseDTO getUserById(UUID userId) {
        var user = userModelDb.findById(userId);
        UserModelResponseDTO userModelDTO = new UserModelResponseDTO();

        if (user.isPresent() && !user.get().isDeleted()) {
            userModelDTO.setId(user.get().getId().toString());
            userModelDTO.setNama(user.get().getNama());
            userModelDTO.setEmail(user.get().getEmail());
            userModelDTO.setRole(user.get().getRole().getRole());
            userModelDTO.setNomorTelefon(user.get().getNomorTelefon());
            userModelDTO.setPassword(user.get().getPassword());

            return userModelDTO;
        }
        else{
            throw new NoSuchElementException("User Not Found");
        }
    }

    @Override
    public UserModel findUserById(UUID id) {
        var user = userModelDb.findById(id);

        if(user.isPresent() && !user.get().isDeleted()) {
            return user.get();
        }else{
            throw new NoSuchElementException("User not found");
        }
    }

    @Override
    public VendorResponseDTOFauzan getVendorById(UUID userId) {
        var vendor = vendorDb.findById(userId);
        VendorResponseDTOFauzan vendorDTO = new VendorResponseDTOFauzan();

        if (vendor.isPresent()) {
            vendorDTO.setId(vendor.get().getId().toString());
            vendorDTO.setNama(vendor.get().getNama());
            vendorDTO.setEmail(vendor.get().getEmail());
            vendorDTO.setRole(vendor.get().getRole().getRole());
            vendorDTO.setNomorTelefon(vendor.get().getNomorTelefon());
            vendorDTO.setPassword(vendor.get().getPassword());

            vendorDTO.setAlamat(vendor.get().getAlamat());
            vendorDTO.setNamaPerusahaan(vendor.get().getNamaPerusahaan());
            vendorDTO.setStatus(vendor.get().getStatus());

            return vendorDTO;
        }
        else{
            throw new NoSuchElementException("User Not Found");
        }
    }

    @Override
    public UserModel createUserModel(AdminCreateUserRequestDTO adminCreateUserRequestDTO) {
        String role = adminCreateUserRequestDTO.getRole();
        String password = userService.encrypt(adminCreateUserRequestDTO.getPassword());

        System.out.println("PASSWORD");
        System.out.println(password);

        if (role.equals("Admin")) {
            Admin admin = new Admin();

            admin.setNama(adminCreateUserRequestDTO.getNama());
            admin.setEmail(adminCreateUserRequestDTO.getEmail());
            admin.setNomorTelefon(adminCreateUserRequestDTO.getNomorTelefon());
            admin.setPassword(password);
            admin.setRole(roleService.getRoleByRoleName("Admin"));
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            return userModelDb.save(admin);

        }else if (role.equals("Procurement Manager")){
            ProcManager procManager = new ProcManager();

            procManager.setNama(adminCreateUserRequestDTO.getNama());
            procManager.setEmail(adminCreateUserRequestDTO.getEmail());
            procManager.setNomorTelefon(adminCreateUserRequestDTO.getNomorTelefon());
            procManager.setPassword(password);
            procManager.setRole(roleService.getRoleByRoleName("Procurement Manager"));
            procManager.setCreatedAt(LocalDateTime.now());
            procManager.setUpdatedAt(LocalDateTime.now());


            return userModelDb.save(procManager);

        }else if (role.equals("Procurement Staff")){
            ProcStaff procStaff = new ProcStaff();

            procStaff.setNama(adminCreateUserRequestDTO.getNama());
            procStaff.setEmail(adminCreateUserRequestDTO.getEmail());
            procStaff.setNomorTelefon(adminCreateUserRequestDTO.getNomorTelefon());
            procStaff.setPassword(password);
            procStaff.setRole(roleService.getRoleByRoleName("Procurement Staff"));
            procStaff.setCreatedAt(LocalDateTime.now());
            procStaff.setUpdatedAt(LocalDateTime.now());

            return userModelDb.save(procStaff);
        } else{
            throw new NoSuchElementException("Role not found");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        UserModel user = userModelDb.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setDeleted(true);
        userModelDb.save(user);
    }

    @Override
    public UserModel updateUser(AdminUpdateUserRequestDTO adminUpdateUserRequestDTO) {
        String role = adminUpdateUserRequestDTO.getRole();
        UUID id = adminUpdateUserRequestDTO.getId();

        if (role.equals("Admin")) {
            Admin existingAdmin = adminDb.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            existingAdmin.setNama(adminUpdateUserRequestDTO.getNama());
            existingAdmin.setEmail(adminUpdateUserRequestDTO.getEmail());
            existingAdmin.setNomorTelefon(adminUpdateUserRequestDTO.getNomorTelefon());
//            existingAdmin.setPassword(adminCreateUserRequestDTO.getPassword());
            existingAdmin.setRole(roleService.getRoleByRoleName("Admin"));
            existingAdmin.setCreatedAt(LocalDateTime.now());
            existingAdmin.setUpdatedAt(LocalDateTime.now());

            return adminDb.save(existingAdmin);

        }else if (role.equals("Procurement Manager")){
            ProcManager existingProcManager = procManagerDb.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            existingProcManager.setNama(adminUpdateUserRequestDTO.getNama());
            existingProcManager.setEmail(adminUpdateUserRequestDTO.getEmail());
            existingProcManager.setNomorTelefon(adminUpdateUserRequestDTO.getNomorTelefon());
//            existingProcManager.setPassword(adminCreateUserRequestDTO.getPassword());
            existingProcManager.setRole(roleService.getRoleByRoleName("Procurement Manager"));
            existingProcManager.setCreatedAt(LocalDateTime.now());
            existingProcManager.setUpdatedAt(LocalDateTime.now());


            return procManagerDb.save(existingProcManager);

        }else if (role.equals("Procurement Staff")){
            ProcStaff existingProcStaff = procStaffDb.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            existingProcStaff.setNama(adminUpdateUserRequestDTO.getNama());
            existingProcStaff.setEmail(adminUpdateUserRequestDTO.getEmail());
            existingProcStaff.setNomorTelefon(adminUpdateUserRequestDTO.getNomorTelefon());
//            existingProcStaff.setPassword(adminCreateUserRequestDTO.getPassword());
            existingProcStaff.setRole(roleService.getRoleByRoleName("Procurement Staff"));
            existingProcStaff.setCreatedAt(LocalDateTime.now());
            existingProcStaff.setUpdatedAt(LocalDateTime.now());

            return userModelDb.save(existingProcStaff);
        }
        else{
            throw new NoSuchElementException("Role not found");
        }
    }

    @Override
    public UserModel updateVendor(UpdateVendorRequestDTO updateVendorRequestDTO) {
        UUID id = updateVendorRequestDTO.getId();

        System.out.println(id);

        if (id != null) {
            Vendor existingVendor = vendorDb.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Vendor not found"));

            existingVendor.setNama(updateVendorRequestDTO.getNama());
            existingVendor.setEmail(updateVendorRequestDTO.getEmail());
            existingVendor.setNomorTelefon(updateVendorRequestDTO.getNomorTelefon());
//            existingVendor.setPassword(updateVendorRequestDTO.getPassword());
            existingVendor.setRole(roleService.getRoleByRoleName("Vendor"));
            existingVendor.setCreatedAt(LocalDateTime.now());
            existingVendor.setUpdatedAt(LocalDateTime.now());

            existingVendor.setNamaPerusahaan(updateVendorRequestDTO.getNamaPerusahaan());
            existingVendor.setAlamat(updateVendorRequestDTO.getAlamat());
            existingVendor.setStatus(updateVendorRequestDTO.getStatus());

            System.out.println(existingVendor.getAlamat());

            return vendorDb.save(existingVendor);
        }

        else{
            throw new NoSuchElementException("Vendor not found");
        }
    }




}
