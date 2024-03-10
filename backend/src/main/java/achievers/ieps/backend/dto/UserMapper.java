package achievers.ieps.backend.dto;

import achievers.ieps.backend.dto.request.CreateUserRequestDTO;
import achievers.ieps.backend.dto.request.CreateVendorRequestDTO;
import achievers.ieps.backend.dto.response.CreateUserResponseDTO;
import achievers.ieps.backend.dto.response.UserModelResponseDTO;
import achievers.ieps.backend.dto.response.VendorResponseDTO;
import achievers.ieps.backend.model.UserModel;
import achievers.ieps.backend.model.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    UserModel createUserRequestDTOToUser(CreateUserRequestDTO createUserRequestDTO);
    @Mapping(target = "role", ignore = true)
    Vendor createVendorRequestDTOToVendor(CreateVendorRequestDTO createVendorRequestDTO);

    @Mapping(target = "role", ignore = true)
    CreateUserResponseDTO createUserResponseDTOToUserModel(UserModel userModel);
    @Mapping(target = "role", ignore = true)
    VendorResponseDTO vendorResponseDTOToVendor(Vendor vendor);

    @Mapping(target = "role", ignore = true)
    UserModelResponseDTO userModelToUserModelResponseDTO(UserModel user);
}
