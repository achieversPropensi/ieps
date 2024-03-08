package achievers.ieps.backend.service;

import achievers.ieps.backend.dto.request.AdminCreateUserRequestDTO;
import achievers.ieps.backend.dto.request.AdminUpdateUserRequestDTO;
import achievers.ieps.backend.dto.request.UpdateVendorRequestDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.dto.response.VendorResponseDTOFauzan;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;

import java.util.UUID;


import java.util.List;

public interface AdminService {

    List<UserModelResponseDTO> getAllUserModel();

    UserModelResponseDTO getUserById(UUID id);

    UserModel findUserById(UUID id);

    VendorResponseDTOFauzan getVendorById(UUID id);

    UserModel createUserModel(AdminCreateUserRequestDTO adminCreateUserRequestDTO);

    public void deleteUser(UUID id);

    UserModel updateUser(AdminUpdateUserRequestDTO adminUpdateUserRequestDTO);

    UserModel updateVendor(UpdateVendorRequestDTO updateVendorRequestDTO);
}
